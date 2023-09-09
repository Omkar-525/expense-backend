package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.BudgetService;
import com.omkar.expensetracker.infra.entity.Budget;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.SetBudgetRequest;
import com.omkar.expensetracker.infra.model.response.GetBudgetResponse;
import com.omkar.expensetracker.infra.repository.BudgetRepository;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.security.CustomUserDetailsService;
import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.AppUtil;
import com.omkar.expensetracker.util.response_builders.BaseFailure;
import com.omkar.expensetracker.util.response_builders.BaseSuccess;
import com.omkar.expensetracker.util.response_builders.failure.FailureResponseBuilder;
import com.omkar.expensetracker.util.response_builders.success.SuccessResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SuccessResponseBuilder successResponse;

    @Autowired
    private FailureResponseBuilder failureResponse;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BaseSuccess baseSuccess;

    @Autowired
    private BaseFailure baseFailure;

    @Override
    public GetBudgetResponse getBudget(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if(user.isPresent()){
                    Optional<Budget> budget = budgetRepository.findByUserAndMonth(user.get(), appUtil.getMonthYear());
                    return successResponse.getBudget(budget.get());
                }
                return failureResponse.getBudget("User not present");
            } catch (Exception ex){
                log.info(ex.getMessage());
                return failureResponse.getBudget("Something went wrong");
            }
        }
        return failureResponse.getBudget("Invalid JWT");
    }

    @Override
    public BaseResponse setBudget(String authorizationHeader, SetBudgetRequest request) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if(user.isPresent()) {
                    Optional<Budget> budget = budgetRepository.findByUserAndMonth(user.get(), request.getMonth());
                    Budget b;
                    if(budget.isPresent()) {
                        b = budget.get();
                        b.setBudgetAmount(request.budget);
                    } else{
                        b = Budget.builder()
                                .budgetAmount(request.budget)
                                .month(request.getMonth())
                                .user(user.get())
                                .build();
                    }
                    budgetRepository.save(b);
                    return baseSuccess.baseSuccessResponse("Budget saved");
                }
            } catch (Exception ex){
                return baseFailure.baseFailResponse("Something went wrong");
            }
        }
        return baseFailure.baseFailResponse("Invalid JWT");
    }


}
