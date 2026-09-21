package com.codefactory.dev_social_network.shared.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class CuentaBloqueadaException extends AuthException {
    public CuentaBloqueadaException(LocalDateTime bloqueadoHasta) {
        super(AuthErrorCode.CUENTA_BLOQUEADA,
              "Cuenta bloqueada temporalmente por intentos fallidos",
              Map.of("bloqueadoHasta", bloqueadoHasta));
    }
}