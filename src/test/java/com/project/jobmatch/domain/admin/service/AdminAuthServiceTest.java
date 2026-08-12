package com.project.jobmatch.domain.admin.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.admin.dto.AdminLoginRequest;
import com.project.jobmatch.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAuthServiceTest {
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtEncoder jwtEncoder;

    @Test
    void invalidCredentialsReturnUnauthorizedWithoutRevealingCause() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        AdminAuthService service = new AdminAuthService(userRepository, passwordEncoder, jwtEncoder, 3600);

        assertThatThrownBy(() -> service.login(new AdminLoginRequest("unknown@example.com", "wrong")))
                .isInstanceOfSatisfying(CustomException.class, exception -> {
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    assertThat(exception.getCode()).isEqualTo("INVALID_CREDENTIALS");
                });
    }
}
