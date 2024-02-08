package com.starbucks.backend.domain.card;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true)
    private Long cardNumber;

    @Column(unique = true)
    private Long pinNumber;

    @Column(length = 1)
    private String isUsable;
}
