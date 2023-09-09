package com.omkar.expensetracker.infra.model.response;

import com.omkar.expensetracker.infra.entity.Budget;
import com.omkar.expensetracker.infra.model.BaseResponse;
import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBudgetResponse extends BaseResponse {

    public Budget budget;

    @Builder
    public GetBudgetResponse(HttpStatus httpStatus, String status, String responseCode, String responseDescription, Budget budget) {
        super(httpStatus, status, responseCode, responseDescription);
        this.budget = budget;
    }
}
