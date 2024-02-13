package com.starbucks.backend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        final UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) authentication;
        final String email = token.getName();
        final String password = (String) token.getCredentials();
        final CustomUserDetails userDetails;

        userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
        boolean isMatched = bCryptPasswordEncoder.matches(password, userDetails.getPassword());
        if (!isMatched) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
