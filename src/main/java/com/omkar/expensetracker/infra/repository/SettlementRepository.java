package com.omkar.expensetracker.infra.repository;

import com.omkar.expensetracker.infra.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
