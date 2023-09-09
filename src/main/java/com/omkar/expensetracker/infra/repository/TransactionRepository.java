package com.omkar.expensetracker.infra.repository;

import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserAndDateContainingAndType(User user, String month, String type);
    List<Transaction> findByUserAndDateContaining(User user, String month);

    Optional<Transaction> findByIdAndUser(Long id, User user);
}
