package com.codefactory.dev_social_network.autenticacion.service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int MAX_INTENTOS = 5;
    private static final int VENTANA_MINUTOS = 15;
    private static final int BLOQUEO_MINUTOS = 30;

    private final UsuarioQueryService usuarioQueryService; // usuarios/interfaces
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountLockRepository accountLockRepository;
    private final AuthTokenRepository authTokenRepository;

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request, String ipAddress, String userAgent) {
        var usuarioOpt = usuarioQueryService.buscarPorEmail(request.getEmail());

        Long usuarioId = usuarioOpt.map(UsuarioDTO::getId).orElse(null);

        if (usuarioId != null && estaCuentaBloqueada(usuarioId)) {
            LocalDateTime hasta = accountLockRepository.findByUsuarioId(usuarioId)
                    .map(AccountLock::getBloqueadoHasta).orElse(null);
            throw new CuentaBloqueadaException(hasta);
        }

        boolean credencialesValidas = usuarioOpt.isPresent()
                && passwordEncoder.matches(request.getPassword(), usuarioOpt.get().getPasswordHash());

        registrarIntento(usuarioId, credencialesValidas, ipAddress, userAgent);

        if (!credencialesValidas) {
            if (usuarioId != null) evaluarBloqueo(usuarioId);
            throw new CredencialesInvalidasException();
        }

        accountLockRepository.resetear(usuarioId);

        String access = jwtProvider.generarAccessToken(usuarioId);
        String refresh = jwtProvider.generarRefreshToken(usuarioId);
        persistirToken(usuarioId, access, AuthToken.TipoToken.ACCESS, jwtProvider.getAccessTtlMinutos());
        persistirToken(usuarioId, refresh, AuthToken.TipoToken.REFRESH, jwtProvider.getRefreshTtlMinutos());

        return new LoginResponseDTO(access, refresh, jwtProvider.getAccessTtlMinutos() * 60L);
    }

    @Override
    @Transactional
    public void logout(String accessToken) {
        String hash = jwtProvider.hash(accessToken);
        authTokenRepository.findByTokenHashAndRevocadoFalse(hash).ifPresentOrElse(t -> {
            t.setRevocado(true);
            t.setRevocadoEn(LocalDateTime.now());
        }, () -> { throw new AuthException(AuthErrorCode.TOKEN_INVALIDO, "Token no encontrado o ya revocado", null); });
    }

    @Override
    @Transactional
    public void revocarTodosLosTokens(Long usuarioId) {
        authTokenRepository.revocarTodosPorUsuario(usuarioId);
    }

    @Override
    public boolean estaCuentaBloqueada(Long usuarioId) {
        return accountLockRepository.findByUsuarioId(usuarioId)
                .map(l -> l.getBloqueadoHasta() != null && l.getBloqueadoHasta().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    // --- privados ---

    private void registrarIntento(Long usuarioId, boolean exitoso, String ip, String ua) {
        if (usuarioId == null) return; // no se registra intento si el email ni existe (evita enumeración de logs inútiles)
        var attempt = new LoginAttempt();
        attempt.setUsuarioId(usuarioId);
        attempt.setExitoso(exitoso);
        attempt.setIpAddress(ip);
        attempt.setUserAgent(ua);
        attempt.setFechaHora(LocalDateTime.now());
        loginAttemptRepository.save(attempt);
    }

    private void evaluarBloqueo(Long usuarioId) {
        LocalDateTime desde = LocalDateTime.now().minusMinutes(VENTANA_MINUTOS);
        long fallos = loginAttemptRepository.contarFallosDesde(usuarioId, desde);

        if (fallos >= MAX_INTENTOS) {
            var lock = accountLockRepository.findByUsuarioId(usuarioId)
                    .orElseGet(() -> {
                        var l = new AccountLock();
                        l.setUsuarioId(usuarioId);
                        return l;
                    });
            lock.setIntentosFallidos((int) fallos);
            lock.setBloqueadoHasta(LocalDateTime.now().plusMinutes(BLOQUEO_MINUTOS));
            lock.setActualizadoEn(LocalDateTime.now());
            accountLockRepository.save(lock);
        }
    }

    private void persistirToken(Long usuarioId, String token, AuthToken.TipoToken tipo, int ttlMinutos) {
        var t = new AuthToken();
        t.setUsuarioId(usuarioId);
        t.setTokenHash(jwtProvider.hash(token));
        t.setTipo(tipo);
        t.setEmitidoEn(LocalDateTime.now());
        t.setExpiraEn(LocalDateTime.now().plusMinutes(ttlMinutos));
        authTokenRepository.save(t);
    }
}