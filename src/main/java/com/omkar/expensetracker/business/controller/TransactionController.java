package com.omkar.expensetracker.business.controller;

import com.omkar.expensetracker.business.service.TransactionService;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.GetTransactionRequest;
import com.omkar.expensetracker.infra.model.request.SetTransactionRequest;
import com.omkar.expensetracker.infra.model.response.GetTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{month}")
    public GetTransactionResponse getTransaction(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                 @PathVariable("month") String month) {
        return transactionService.getTransaction(authorizationHeader, month, "DEBIT");
    }

    @GetMapping("/all/{month}")
    public GetTransactionResponse getAllTransaction(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                 @PathVariable("month") String month) {
        return transactionService.getTransaction(authorizationHeader, month, null);
    }

    @PostMapping
    public BaseResponse setTransaction(@RequestHeader(value = "Authorization") String authorizationHeader,
                                        @RequestBody SetTransactionRequest request){
        return transactionService.setTransaction(authorizationHeader,request);
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteTransaction(@RequestHeader(value = "Authorization") String authorizationHeader,
                                          @PathVariable("id") Long id){
        return transactionService.deleteTransaction(authorizationHeader,id);
    }

}
