package com.project.jobmatch.domain.admin.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.admin.dto.AdminLoginRequest;
import com.project.jobmatch.domain.admin.dto.AdminLoginResponse;
import com.project.jobmatch.domain.user.entity.User;
import com.project.jobmatch.domain.user.entity.UserRole;
import com.project.jobmatch.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional(readOnly = true)
public class AdminAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public AdminAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                            JwtEncoder jwtEncoder,
                            @Value("${app.jwt.expiration-seconds}") long expirationSeconds) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.expirationSeconds = expirationSeconds;
    }

    public AdminLoginResponse login(AdminLoginRequest request) {
        User admin = userRepository.findByEmail(request.email())
                .filter(user -> user.getRole() == UserRole.ADMIN)
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .orElseThrow(this::invalidCredentials);

        Instant issuedAt = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("jobmatch-api")
                .subject(admin.getEmail())
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(expirationSeconds))
                .claim("role", admin.getRole().name())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new AdminLoginResponse(token);
    }

    private CustomException invalidCredentials() {
        return new CustomException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS",
                "이메일 또는 비밀번호가 올바르지 않습니다.");
    }
}
