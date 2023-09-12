package com.omkar.expensetracker.infra.model.response;

import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.MonthlyTransactions;
import com.omkar.expensetracker.infra.model.TotalExpense;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDashboardResponse extends BaseResponse {

    private MonthlyTransactions monthlyTransactions;

    private TotalExpense totalExpense;

    @Builder
    public GetDashboardResponse(HttpStatus httpStatus, String status, String responseCode, String responseDescription, MonthlyTransactions monthlyTransactions, TotalExpense totalExpense) {
        super(httpStatus, status, responseCode, responseDescription);
        this.monthlyTransactions = monthlyTransactions;
        this.totalExpense = totalExpense;
    }
}



