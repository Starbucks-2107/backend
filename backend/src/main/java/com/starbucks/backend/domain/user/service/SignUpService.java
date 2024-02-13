package com.starbucks.backend.domain.user.service;

import com.starbucks.backend.domain.user.dto.SignUpRequest;
import com.starbucks.backend.domain.user.entity.Membership;
import com.starbucks.backend.domain.user.entity.Role;
import com.starbucks.backend.domain.user.entity.User;
import com.starbucks.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {
        Optional<User> checkUser = userRepository.findByEmail(request.getNickname());
        if(checkUser.isPresent()) {
            throw new EntityExistsException();
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .nickname(request.getNickname())
                .birthday(request.getBirthday())
                .isGotBirthdayCoupon("N")
                .membership(Membership.NONE)
                .role(Role.USER)
                .joinedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}
