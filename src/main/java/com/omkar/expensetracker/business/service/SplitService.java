package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.entity.Split;
import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.CreditorDebts;
import com.omkar.expensetracker.infra.model.request.CreateSplitRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SplitService {
    Split createSplit(String authorizationHeader, CreateSplitRequest userIds);

    Split getSplit(String authorizationHeader, Long splitId);

    Transaction addTransactionToSplit(String authorizationHeader, Long splitId, Transaction transaction);

    List<Transaction> getTransactionsOfSplit(String authorizationHeader, Long splitId);

    List<CreditorDebts> getDebtSummary(String authorizationHeader, Long splitId);

    Split finalizeSplit(String authorizationHeader, Long splitId);

    List<Split> getAllSplitsForUser(String authorizationHeader);

    ResponseEntity<BaseResponse> addUsersToSplit(String authorizationHeader, Long splitId, List<Long> userIds);
}
