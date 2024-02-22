package org.starbucks.backend.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.starbucks.backend.global.jwt.util.TokenUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response
            , @NotNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (
                requestURI.equals("/api/v1/auth/login") ||
                requestURI.startsWith("/api/v1/auth/token") ||
                requestURI.startsWith("/api/v1/data") ||
                requestURI.startsWith("/api/v1/docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authorizationHeader != null) {
            accessToken = authorizationHeader;
        }

        tokenUtil.validateAccessToken(accessToken);
        tokenUtil.getAuthenticationUsingToken(accessToken);
        filterChain.doFilter(request, response);
    }
}
