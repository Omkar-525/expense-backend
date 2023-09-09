package com.omkar.expensetracker.business.controller;

import com.omkar.expensetracker.business.service.BudgetService;
import com.omkar.expensetracker.infra.entity.Budget;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.SetBudgetRequest;
import com.omkar.expensetracker.infra.model.response.GetBudgetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user/budget")
public class BudgetController {


    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public GetBudgetResponse getBudget(@RequestHeader(value = "Authorization") String authorizationHeader){
        return budgetService.getBudget(authorizationHeader);
    }

    @PostMapping
    public BaseResponse setBudget(@RequestHeader(value = "Authorization") String authorizationHeader,
                                  @RequestBody SetBudgetRequest request){
        return budgetService.setBudget(authorizationHeader, request);
    }
}
