package com.carlos.auth.service;
import com.carlos.auth.dto.AuthResponse;
import com.carlos.auth.dto.LoginRequest;
import com.carlos.auth.model.RefreshToken;
import com.carlos.auth.model.User;
import com.carlos.auth.repository.UserRepository;
import com.carlos.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${pagoya.security.jwt.expiration-ms}")
    private long accessExpirationMs;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String role = user.getRole().getName();

        String accessToken = jwtService.generateToken(userDetails, role);
        RefreshToken refresh = refreshTokenService.create(user);

        return new AuthResponse(
                accessToken, refresh.getToken(),
                user.getEmail(), role, accessExpirationMs);
    }

    /**
     * Renueva el access token aplicando rotation: el refresh recibido se
     * revoca y se emite uno nuevo, junto con el nuevo access.
     */
    @Override
    public AuthResponse refresh(String refreshToken) {
        RefreshToken current = refreshTokenService.validate(refreshToken);
        RefreshToken rotated = refreshTokenService.rotate(current);

        User user = rotated.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String role = user.getRole().getName();

        String newAccessToken = jwtService.generateToken(userDetails, role);
        return new AuthResponse(
                newAccessToken, rotated.getToken(),
                user.getEmail(), role, accessExpirationMs);
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }

    /**
     * Cierra sesion en todos los dispositivos del usuario autenticado.
     * Revoca todos sus refresh tokens activos.
     */
    @Override
    public void logoutAll(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("usuario no encontrado"));
        refreshTokenService.revokeAllByUserId(user.getId());
    }
}