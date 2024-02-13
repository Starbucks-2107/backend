package com.starbucks.backend.domain.user.entity;

import com.starbucks.backend.domain.user.dto.ChangeUserInfoRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    private String password;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(length = 11, unique = true)
    private String phoneNumber;

    @NotNull
    @Pattern(regexp = "^[가-힣]+$")
    @Size(min = 1, max = 6)
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private Membership membership;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Pattern(regexp = "^[0-9]+$")
    @Column(length = 8)
    private String birthday;

    @Column(length = 1)
    private String isGotBirthdayCoupon;

    private LocalDateTime joinedAt;


    // ==============================================================================

    public void updateUserInfo(ChangeUserInfoRequest request) {
        Optional.ofNullable(request.getNewPhoneNumber()).ifPresent(this::setPhoneNumber);
        Optional.ofNullable(request.getNewNickname()).ifPresent(this::setNickname);
        Optional.ofNullable(request.getNewUsername()).ifPresent(this::setUsername);
        Optional.ofNullable(request.getNewBirthday()).ifPresent(this::setBirthday);
    }

    private void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    // ------------------------------------------------------------------------------

    public void updatePassword(String password) {
        this.password = password;
    }
}
