package com.VigiDrive.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Slf4j
public class UserDetailsImpl implements UserDetails {

    @Getter
    private String name;
    @Getter
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;


    public UserDetailsImpl(String email, String name, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this(email, name, password, authorities, true, true, true, true);
    }

    public UserDetailsImpl(String email, String name, String password,
                           Collection<? extends GrantedAuthority> authorities, Boolean enabled,
                           Boolean accountNonExpired,
                           Boolean accountNonLocked, boolean credentialsNonExpired) {
        log.error("---------- 2 --------------------");
        this.email = email;
        this.name = name;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) o;
        return getPassword().equals(userDetails.getPassword())
                && getUsername().equals(userDetails.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassword(), getUsername());
    }
}
