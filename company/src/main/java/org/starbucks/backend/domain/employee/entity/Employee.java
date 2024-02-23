package org.starbucks.backend.domain.employee.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    // TODO: 더 추가 해야할 필드가 있나?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private String idNumber;

    private String name;

    private String password;

    @Email
    private String email;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    @Column(length = 11, unique = true)
    private String phoneNumber;

    private String department;

    private String team;
}
