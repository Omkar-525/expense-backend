package com.omkar.expensetracker.infra.repository;

import com.omkar.expensetracker.infra.entity.Split;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SplitRepository extends JpaRepository<Split, Long> {
}
