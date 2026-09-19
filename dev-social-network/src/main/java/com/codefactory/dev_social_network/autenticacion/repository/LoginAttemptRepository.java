package com.codefactory.dev_social_network.autenticacion.repository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    void save(LoginAttempt attempt); // heredado, explícito por claridad de uso

    @Query("SELECT COUNT(a) FROM LoginAttempt a " +
           "WHERE a.usuarioId = :usuarioId AND a.exitoso = false AND a.fechaHora >= :desde")
    long contarFallosDesde(@Param("usuarioId") Long usuarioId, @Param("desde") LocalDateTime desde);

    Optional<LoginAttempt> findTopByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);
}
