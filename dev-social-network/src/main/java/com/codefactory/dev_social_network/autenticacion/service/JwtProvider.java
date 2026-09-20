package com.codefactory.dev_social_network.autenticacion.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-ttl-minutos:15}")
    private int accessTtlMinutos;

    public String generarAccessToken(UUID usuarioId) {
        SecretKey key = getKey();
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + accessTtlMinutos * 60_000L);

        return Jwts.builder()
                .subject(usuarioId.toString())
                .issuedAt(ahora)
                .expiration(expira)
                .signWith(key)
                .compact();
    }

    public boolean esTokenValido(String token) {
        try {
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID obtenerUsuarioId(String token) {
        String subject = Jwts.parser().verifyWith(getKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
        return UUID.fromString(subject);
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public int getAccessTtlMinutos() {
        return accessTtlMinutos;
    }
}