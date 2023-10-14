package com.omkar.expensetracker.util.response_builders.failure;

import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.response.*;
import com.omkar.expensetracker.util.response_builders.BaseFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FailureResponseBuilder {

    @Autowired
    private BaseFailure baseFailure;
    public LoginResponse loginFailed(String description) {
        BaseResponse baseResponse = baseFailure.baseFailResponse(description);
        return LoginResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .build();
    }

    public GetBudgetResponse getBudget(String description) {
        BaseResponse baseResponse = baseFailure.baseFailResponse(description);
        return GetBudgetResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .build();
    }

    public GetTransactionResponse getTransaction(String description) {
        BaseResponse baseResponse = baseFailure.baseFailResponse(description);
        return GetTransactionResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .build();
    }

    public GetDashboardResponse dashboardInfo(String description) {
        BaseResponse baseResponse = baseFailure.baseFailResponse(description);
        return GetDashboardResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .build();
    }

    public ProfileResponse getUser(String description) {
        BaseResponse baseResponse = baseFailure.baseFailResponse(description);
        return ProfileResponse.builder()
                .status(baseResponse.getStatus())
                .responseDescription(baseResponse.getResponseDescription())
                .responseCode(baseResponse.getResponseCode())
                .httpStatus(baseResponse.getHttpStatus())
                .build();
    }

}
