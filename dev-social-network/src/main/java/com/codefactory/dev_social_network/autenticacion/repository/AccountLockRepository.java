package com.codefactory.dev_social_network.autenticacion.repository;

public interface AccountLockRepository extends JpaRepository<AccountLock, Long> {

    Optional<AccountLock> findByUsuarioId(Long usuarioId);

    @Modifying
    @Query("UPDATE AccountLock a SET a.intentosFallidos = 0, a.bloqueadoHasta = null, " +
           "a.actualizadoEn = CURRENT_TIMESTAMP WHERE a.usuarioId = :usuarioId")
    void resetear(@Param("usuarioId") Long usuarioId);
}