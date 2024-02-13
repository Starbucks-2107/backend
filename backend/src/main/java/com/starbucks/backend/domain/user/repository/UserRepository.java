package com.starbucks.backend.domain.user.repository;

import com.starbucks.backend.domain.user.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @NotNull
    Optional<User> findById(Long id);
    Optional<User> findUserByUsernameAndPhoneNumber(String username, String phoneNumber);
}
