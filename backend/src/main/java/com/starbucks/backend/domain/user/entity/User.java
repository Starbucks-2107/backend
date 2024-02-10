package com.starbucks.backend.domain.user.entity;

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

    private LocalDateTime joinedAt;
}
