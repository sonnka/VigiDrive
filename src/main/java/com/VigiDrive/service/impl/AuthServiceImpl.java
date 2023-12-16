package com.VigiDrive.service.impl;

import com.VigiDrive.model.response.LoginResponse;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private JwtEncoder jwtEncoder;

    @Override
    public LoginResponse login(Authentication authentication) {
        return getLoginResponse(authentication.getName());

    }

    private LoginResponse getLoginResponse(String email) {
        var user = userRepository.findByEmailIgnoreCase(email).get();
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(email)
                .claim("id", user.getId())
                .build();

        log.error("Logged in = {}", email);

        return LoginResponse.builder()
                .id(user.getId())
                .token(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .name(user.getFirstName())
                .surname(user.getLastName())
                .avatar(user.getAvatar())
                .build();
    }

    private String createScope(Collection<String> authorities) {
        return authorities.stream().findFirst().stream().collect(Collectors.joining(" "));
    }

}
