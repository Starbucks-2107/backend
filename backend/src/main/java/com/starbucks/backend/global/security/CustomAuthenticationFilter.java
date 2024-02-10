package com.starbucks.backend.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbucks.backend.domain.user.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        final UsernamePasswordAuthenticationToken authenticationToken;

        try {
            final LoginRequest loginRequest
                    = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            log.info("======================================================");
            log.info("email " + loginRequest.email());
            log.info("======================================================");

            authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        } catch (IOException e) {
            throw new AuthenticationServiceException("사용자 인증에 실패 하였 습니다.");
        }

        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
