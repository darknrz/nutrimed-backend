package com.tuapp.nutrimed_backend.user.repository;

import com.tuapp.nutrimed_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.tuapp.nutrimed_backend.user.dto.UserProfileDto;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername(String email, String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}