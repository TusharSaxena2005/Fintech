package com.fintech.Backend.repository;

import com.fintech.Backend.entity.TransactionEntity;
import com.fintech.Backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserOrderByTransactionDateDesc(UserEntity user);
    List<TransactionEntity> findTop10ByUserOrderByTransactionDateDesc(UserEntity user);
}