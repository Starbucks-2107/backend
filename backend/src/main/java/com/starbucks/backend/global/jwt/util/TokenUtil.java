package com.starbucks.backend.global.jwt.util;

import com.starbucks.backend.domain.user.entity.User;
import com.starbucks.backend.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtil implements InitializingBean {
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    static final long AccessTokenValidTime = 15 * 60 * 1000L;

    @Value("${secret.key}")
    private String SECRET_KEY;
    private Key key;


    @Override
    public void afterPropertiesSet() {
        generateKey();
    }

    private void generateKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // accessToken ---------------------------------------------------
    public String generateAccessToken(String userId) {
        if (key == null) {
            generateKey();
        }

        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AccessTokenValidTime))
                .signWith(key)
                .compact();

        return "Bearer " + accessToken;
    }

    // refreshToken -------------------------------------------------
    public String generateRefreshToken(String userId) {
        if (key == null) {
            generateKey();
        }

        Claims claims = Jwts.claims().setSubject(userId);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();

        redisUtil.setData(userId, refreshToken);

        return refreshToken;
    }


    // common method -------------------------------------------------
    // Token Verification.
    public void validateAccessToken(String token) throws ExpiredJwtException {
        token = token.substring(7);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    // Get authentication using Token.
    public void getAuthenticationUsingToken(String accessToken) {
        accessToken = accessToken.substring(7);

        Long userId = getUserIdFromToken(accessToken);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, accessToken, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // Get accessToken from Header.
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessTokenFromHeader = request
                .getHeader("Authorization")
                .substring(7);

        log.info("======================================================");
        log.info("accessTokenFromHeader: " + accessTokenFromHeader);
        log.info("======================================================");

        if (!accessTokenFromHeader.isEmpty()) {
            return accessTokenFromHeader;
        }

        return null;
    }

    // Get userId from token.
    public Long getUserIdFromToken(String token) {
        // Remove "Bearer " from accessToken.
        if (token.contains("Bearer")) {
            token = token.substring(7);
        }

        String userIdFromToken = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        Long userId = Long.parseLong(userIdFromToken);
        log.info("======================================================");
        log.info("userId: " + userId);
        log.info("======================================================");

        return userId;
    }
}
