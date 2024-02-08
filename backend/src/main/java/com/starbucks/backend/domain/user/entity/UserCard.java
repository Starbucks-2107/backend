package com.starbucks.backend.domain.user.entity;

import com.starbucks.backend.domain.card.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private boolean bookMarked;

    @Column(length = 1)
    private String isUsable;

    // ==============================================

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;
}
