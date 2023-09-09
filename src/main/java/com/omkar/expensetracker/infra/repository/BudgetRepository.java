package com.omkar.expensetracker.infra.repository;

import com.omkar.expensetracker.infra.entity.Budget;
import com.omkar.expensetracker.infra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {


     Optional<Budget> findByUserAndMonth(User user, String Month);

}