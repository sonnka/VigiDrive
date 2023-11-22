package com.VigiDrive.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class CustomUserDetails implements OAuth2User {
    @Getter
    private String email;

    @Getter
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String email,
                             Collection<? extends GrantedAuthority> authorities) {
        log.error("---------- 2 --------------------");
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getName() {
        return email;
    }

}