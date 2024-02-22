package org.starbucks.backend.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.starbucks.backend.domain.employee.entity.Employee;

import java.util.Collection;
import java.util.Collections;

public record CustomUserDetails(Employee employee) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: role 늘어 나면 수정 해야 함.
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getIdNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
