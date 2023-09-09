package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.entity.Split;
import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.entity.Debt;
import com.omkar.expensetracker.infra.model.request.CreateSplitRequest;

import java.util.List;

public interface SplitService {
    Split createSplit(String authorizationHeader, CreateSplitRequest userIds);

    Split getSplit(String authorizationHeader, Long splitId);

    Transaction addTransactionToSplit(String authorizationHeader, Long splitId, Transaction transaction);

    List<Transaction> getTransactionsOfSplit(String authorizationHeader, Long splitId);

    List<Debt> getDebtSummary(String authorizationHeader, Long splitId);

    Split finalizeSplit(String authorizationHeader, Long splitId);

    List<Split> getAllSplitsForUser(String authorizationHeader);
}
