package com.omkar.expensetracker.util.response_builders.success;


import com.omkar.expensetracker.infra.entity.Budget;
import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.TransactionDTO;
import com.omkar.expensetracker.infra.model.response.GetBudgetResponse;
import com.omkar.expensetracker.infra.model.response.GetTransactionResponse;
import com.omkar.expensetracker.infra.model.response.LoginResponse;
import com.omkar.expensetracker.util.response_builders.BaseSuccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SuccessResponseBuilder {

    @Autowired
    private BaseSuccess baseSuccess;

    public LoginResponse loginSuccess(String jwt, User user){
        BaseResponse baseResponse = baseSuccess.baseSuccessResponse("Login Successful");
        return LoginResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .jwt(jwt)
                .user(user)
                .build();

    }

    public GetBudgetResponse getBudget(Budget budget) {
        BaseResponse baseResponse = baseSuccess.baseSuccessResponse("Budget Success");
        return GetBudgetResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .budget(budget)
                .build();
    }

    public GetTransactionResponse getTransaction(List<Transaction> transactions) {
        BaseResponse baseResponse = baseSuccess.baseSuccessResponse("Get Transaction Success");
        return GetTransactionResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .transactions(transactions.stream().map(TransactionDTO::new).collect(Collectors.toList()))
                .build();
    }
}
