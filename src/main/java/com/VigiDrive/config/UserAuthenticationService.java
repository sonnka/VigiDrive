package com.VigiDrive.config;

import com.VigiDrive.model.entity.User;
import com.VigiDrive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByEmail(username.toLowerCase());
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(grantedAuthority);
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .enabled(true)
                .build();
    }

    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Username %s not found".formatted(email)));
    }
}