package com.starbucks.backend.domain.user.controller;

import com.starbucks.backend.domain.user.dto.ChangePasswordRequest;
import com.starbucks.backend.domain.user.dto.ChangeUserInfoRequest;
import com.starbucks.backend.domain.user.dto.UserInfoResponse;
import com.starbucks.backend.domain.user.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user/info")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @GetMapping(value = "")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        UserInfoResponse response = userInfoService.getUserInfo(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/change")
    public void changeUserInfo(@RequestBody ChangeUserInfoRequest changeUserInfoRequest, HttpServletRequest request) {
        userInfoService.changeUserInfo(changeUserInfoRequest, request);
    }

    @PatchMapping(value = "/change/password")
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) throws Exception {
        userInfoService.changePassword(changePasswordRequest, request);
    }
}
