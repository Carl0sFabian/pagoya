package com.carlos.auth.service;

import com.carlos.auth.exception.InvalidRefreshTokenException;
import com.carlos.auth.model.RefreshToken;
import com.carlos.auth.model.User;
import com.carlos.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${pagoya.security.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Transactional
    public RefreshToken create(User user) {
        RefreshToken rt = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(LocalDateTime.now().plusNanos(refreshExpirationMs * 1_000_000))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();
        return refreshTokenRepository.save(rt);
    }

    @Transactional
    public RefreshToken validate(String token) {
        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .orElseThrow(InvalidRefreshTokenException::new);

        // Si ya fue revocado, podria ser un intento de reuso (robo).
        // Defensa: revocar todos los refresh del usuario y forzar relogin.
        if (rt.isRevoked()) {
            refreshTokenRepository.revokeAllByUserId(rt.getUser().getId());
            throw new InvalidRefreshTokenException();
        }
        if (rt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException();
        }
        return rt;
    }

    /**
     * Rotacion: revoca el refresh actual y emite uno nuevo para el mismo usuario.
     * Se llama desde AuthService.refresh() en cada renovacion.
     */
    @Transactional
    public RefreshToken rotate(RefreshToken current) {
        current.setRevoked(true);
        refreshTokenRepository.save(current);
        return create(current.getUser());
    }

    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    /**
     * Revoca TODOS los refresh tokens del usuario.
     * Util para logout-all (cerrar sesion en todos los dispositivos)
     * y como defensa cuando se detecta reuso de un token revocado.
     */
    @Transactional
    public void revokeAllByUserId(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }
}
