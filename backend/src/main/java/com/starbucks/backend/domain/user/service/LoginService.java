package com.starbucks.backend.domain.user.service;

import com.starbucks.backend.domain.user.dto.LoginRequest;
import com.starbucks.backend.domain.user.entity.User;
import com.starbucks.backend.domain.user.repository.UserRepository;
import com.starbucks.backend.global.jwt.util.CookieUtil;
import com.starbucks.backend.global.jwt.util.RedisUtil;
import com.starbucks.backend.global.jwt.util.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.email()).get();
        String userId = String.valueOf(user.getId());

        authenticateUser(request);
        addAccessTokenToHeader(userId, response);
        addRefreshTokenToCookie(userId, response);
    }


    // private method -------------------------------------------------
    private void authenticateUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
    }

    private void addAccessTokenToHeader(String userId, HttpServletResponse response) {
        String accessToken = tokenUtil.generateAccessToken(userId);
        response.setHeader("Authorization", accessToken);
    }

    private void addRefreshTokenToCookie(String userId, HttpServletResponse response) {
        String refreshToken = redisUtil.getData(userId);

        if (refreshToken == null) {
            refreshToken = tokenUtil.generateRefreshToken(userId);

            cookieUtil.create(refreshToken, response);
            redisUtil.setData(userId, refreshToken);
        } else {
            cookieUtil.create(refreshToken, response);
        }
    }
}
