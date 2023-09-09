package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.GetTransactionRequest;
import com.omkar.expensetracker.infra.model.request.SetBudgetRequest;
import com.omkar.expensetracker.infra.model.request.SetTransactionRequest;
import com.omkar.expensetracker.infra.model.response.GetTransactionResponse;

public interface TransactionService {
     GetTransactionResponse getTransaction(String authorizationHeader, String month, String type);

     BaseResponse setTransaction(String authorizationHeader, SetTransactionRequest request);

     BaseResponse deleteTransaction(String authorizationHeader, Long id);
}
