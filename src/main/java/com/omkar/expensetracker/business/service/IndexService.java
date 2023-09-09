package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.LoginRequest;
import com.omkar.expensetracker.infra.model.request.RegisterRequest;
import com.omkar.expensetracker.infra.model.response.LoginResponse;

public interface IndexService {
    LoginResponse login(LoginRequest request);

    BaseResponse register(RegisterRequest request);
}
