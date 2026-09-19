package com.codefactory.dev_social_network.autenticacion.repository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByTokenHashAndRevocadoFalse(String tokenHash);

    List<AuthToken> findByUsuarioIdAndRevocadoFalse(Long usuarioId);

    @Modifying
    @Query("UPDATE AuthToken t SET t.revocado = true, t.revocadoEn = CURRENT_TIMESTAMP " +
           "WHERE t.usuarioId = :usuarioId AND t.revocado = false")
    int revocarTodosPorUsuario(@Param("usuarioId") Long usuarioId);

    // Job de limpieza (no CRUD puro): borra tokens expirados hace más de N días
    @Modifying
    @Query("DELETE FROM AuthToken t WHERE t.expiraEn < :antesDe")
    int purgarExpirados(@Param("antesDe") LocalDateTime antesDe);
}