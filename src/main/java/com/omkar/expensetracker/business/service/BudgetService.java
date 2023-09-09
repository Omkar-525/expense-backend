package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.entity.Budget;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.SetBudgetRequest;
import com.omkar.expensetracker.infra.model.response.GetBudgetResponse;

public interface BudgetService {
    GetBudgetResponse getBudget(String authorizationHeader);

    BaseResponse setBudget(String authorizationHeader, SetBudgetRequest request);
}
