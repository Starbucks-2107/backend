package com.starbucks.backend.domain.user.service;

import com.starbucks.backend.domain.user.dto.ChangePasswordRequest;
import com.starbucks.backend.domain.user.dto.ChangeUserInfoRequest;
import com.starbucks.backend.domain.user.dto.UserInfoResponse;
import com.starbucks.backend.domain.user.entity.User;
import com.starbucks.backend.domain.user.repository.UserRepository;
import com.starbucks.backend.global.jwt.util.TokenUtil;
import com.starbucks.backend.global.util.MaskingUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final MaskingUtil maskingUtil;
    private final PasswordEncoder encoder;

    public UserInfoResponse getUserInfo(HttpServletRequest request) {
        String accessToken = tokenUtil.getAccessTokenFromHeader(request);
        Long userId = tokenUtil.getUserIdFromToken(accessToken);
        User user = userRepository.findById(userId).get();

        UserInfoResponse response = new UserInfoResponse(
                // TODO: 별 개수 추가 하기
                user.getId(),
                maskingUtil.maskingEmail(user.getEmail()),
                user.getNickname(),
                maskingUtil.maskingUsername(user.getUsername()),
                maskingUtil.maskingBirthday(user.getBirthday()),
                maskingUtil.maskingPhoneNumber(user.getPhoneNumber())
        );

        return response;
    }


    @Transactional
    public void changeUserInfo(ChangeUserInfoRequest changeUserInfoRequest, HttpServletRequest request) {
        User user = getUser(changeUserInfoRequest.getUsername(), changeUserInfoRequest.getPhoneNumber(), request);

        user.updateUserInfo(changeUserInfoRequest);
        userRepository.save(user);
    }
    
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest request) throws Exception {
        User user = getUser(changePasswordRequest.getUsername(), changePasswordRequest.getPhoneNumber(), request);

        if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getPasswordCheck())) {
            throw new BadRequestException("Passwords do not match");
        }

        String encodedPassword = encoder.encode(changePasswordRequest.getPassword());

        log.info("======================================================");
        log.info("prevPassword: " + user.getPassword() + " \n requestedPassword: " + encodedPassword);
        log.info("======================================================");

        user.updatePassword(encodedPassword);
    }

    private User getUser(String username, String phoneNumber, HttpServletRequest request) {
        User user
                = userRepository.findUserByUsernameAndPhoneNumber(username, phoneNumber)
                .orElseThrow(EntityNotFoundException::new);

        log.info("======================================================");
        log.info("username: " + username + " phoneNumber: " + phoneNumber);
        log.info("======================================================");

        String accessToken = tokenUtil.getAccessTokenFromHeader(request);
        Long userId = tokenUtil.getUserIdFromToken(accessToken);

        if (!userId.equals(user.getId())) {
            throw new IllegalArgumentException();
        }

        return user;
    }

}
