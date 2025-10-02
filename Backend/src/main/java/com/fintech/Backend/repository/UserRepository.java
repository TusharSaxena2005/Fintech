package com.fintech.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fintech.Backend.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
