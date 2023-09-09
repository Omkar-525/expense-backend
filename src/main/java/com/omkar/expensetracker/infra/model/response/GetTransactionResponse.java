package com.omkar.expensetracker.infra.model.response;

import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.TransactionDTO;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTransactionResponse extends BaseResponse {

    public List<TransactionDTO> transactions;

    @Builder
    public GetTransactionResponse(HttpStatus httpStatus, String status, String responseCode, String responseDescription, List<TransactionDTO> transactions) {
        super(httpStatus, status, responseCode, responseDescription);
        this.transactions = transactions;
    }
}
