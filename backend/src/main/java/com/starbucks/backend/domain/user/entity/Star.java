package com.starbucks.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdAt;

    private LocalDate expiredAt;

    @Column(length = 1)
    // TODO: 추려서 노션에 정리 하기
    private String isUsable;

    // ==============================================

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
