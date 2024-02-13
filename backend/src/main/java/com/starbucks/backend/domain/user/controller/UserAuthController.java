package com.starbucks.backend.domain.user.controller;

import com.starbucks.backend.domain.user.dto.LoginRequest;
import com.starbucks.backend.domain.user.dto.SignUpRequest;
import com.starbucks.backend.domain.user.service.LoginService;
import com.starbucks.backend.domain.user.service.SignUpService;
import com.starbucks.backend.global.jwt.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {
    private final SignUpService signUpService;
    private final LoginService loginService;
    private final CookieUtil cookieUtil;

    @PostMapping(value = "/signup")
    public void signup(@RequestBody @Valid SignUpRequest signUpRequest) {
        signUpService.signUp(signUpRequest);
    }

    @PostMapping(value = "/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        loginService.login(loginRequest, response);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletResponse response) {
        response.setHeader("Authorization", "");
        cookieUtil.delete("", response);
    }
}
