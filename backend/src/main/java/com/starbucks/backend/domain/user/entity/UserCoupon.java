package com.starbucks.backend.domain.user.entity;

import com.starbucks.backend.domain.coupon.Coupon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate CreatedAt;

    private LocalDate expiredAt;

    @Column(length = 1)
    private String isUsable;


    // ==============================================

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
}
