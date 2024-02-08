package com.starbucks.backend.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")
    // 비밀번호는 최소 8자 이상 이며, 숫자, 대문자, 소문자를 적어도 하나씩 포함 해야 합니다.
    // TODO: 예외 처리 알아보기
    private String password;

    // TODO: 닉네임 정책 알아보기
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private Membership membership;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private LocalDateTime joinedAt;
}
