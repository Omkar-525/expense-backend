package com.omkar.expensetracker.business.controller;

import com.omkar.expensetracker.business.service.SplitService;
import com.omkar.expensetracker.infra.entity.Debt;
import com.omkar.expensetracker.infra.entity.Split;
import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.exception.InvalidJwtException;
import com.omkar.expensetracker.infra.exception.UserNotFoundException;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.CreditorDebts;
import com.omkar.expensetracker.infra.model.request.AddUserToSplitRequest;
import com.omkar.expensetracker.infra.model.request.CreateSplitRequest;
import com.omkar.expensetracker.infra.model.response.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping("/user/split")
public class SplitController {

    @Autowired
    private SplitService splitService;

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<DataResponse<Object>> handleInvalidJwtException(InvalidJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DataResponse<>(HttpStatus.UNAUTHORIZED, "Failure", "401", ex.getMessage(), null));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DataResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse<>(HttpStatus.NOT_FOUND,
                "Failure", "404", ex.getMessage(), null));
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<Split>>> getAllSplitsForUser(@RequestHeader(value = "Authorization")
                                                                             String authorizationHeader) {
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200",
                "Splits fetched successfully", splitService.getAllSplitsForUser(authorizationHeader)));
    }

    @PostMapping
    public ResponseEntity<DataResponse<Split>> createSplit(@RequestHeader(value = "Authorization")
                                                               String authorizationHeader, @RequestBody CreateSplitRequest request) {
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200",
                "Split created successfully", splitService.createSplit(authorizationHeader, request)));
    }

    @GetMapping("/{splitId}")
    public ResponseEntity<DataResponse<Split>> getSplit(@RequestHeader(value = "Authorization")
                                                            String authorizationHeader, @PathVariable Long splitId) {
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200",
                "Split fetched successfully", splitService.getSplit(authorizationHeader, splitId)));
    }

    @PostMapping("/{splitId}/transactions")
    public ResponseEntity<DataResponse<Transaction>> addTransactionToSplit(
            @RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable Long splitId, @RequestBody Transaction transaction) {
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200",
                "Transaction added successfully", splitService.addTransactionToSplit(authorizationHeader,
                splitId, transaction)));
    }

    @GetMapping("/{splitId}/transactions")
    public ResponseEntity<DataResponse<List<Transaction>>> getTransactionsOfSplit(
            @RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable Long splitId) {
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200",
                "Transactions fetched successfully",
                splitService.getTransactionsOfSplit(authorizationHeader, splitId)));
    }
    @PutMapping("/addUsers/{splitId}")
    public ResponseEntity<BaseResponse> addUsersToSplit(@RequestHeader(value = "Authorization") String authorizationHeader,
            @PathVariable Long splitId, @RequestBody AddUserToSplitRequest request) {
        return splitService.addUsersToSplit(authorizationHeader,splitId, request.getUserIds());
    }

    @GetMapping("/{splitId}/debts")
    public ResponseEntity<DataResponse<List<CreditorDebts>>> getDebtSummary(
            @RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable Long splitId) {
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200",
                "Debt summary fetched successfully", splitService.getDebtSummary(authorizationHeader, splitId)));
    }

    @PutMapping("/{splitId}/finalize")
    public ResponseEntity<DataResponse<Split>> finalizeSplit(
            @RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable Long splitId) {

        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200",
                "Split finalized successfully", splitService.finalizeSplit(authorizationHeader, splitId)));
    }

}
