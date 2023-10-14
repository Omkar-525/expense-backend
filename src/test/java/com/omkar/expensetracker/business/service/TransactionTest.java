package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.UserDetailsModel;
import com.omkar.expensetracker.infra.model.response.GetTransactionResponse;
import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.enums.ResponseEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionTest {

    @Autowired
    JwtUtil jwtUtil;

    String jwt;

    @Autowired
    TransactionService transactionService;

    @BeforeEach
    void setupJwt(){
        User user = User.builder()
                .email("test@test.com")
                .password("skndkfnspen")
                .active(true)
                .role("User")
                .build();
        UserDetailsModel userDetails = new UserDetailsModel(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        jwt = jwtUtil.generateToken(userDetails,claims);
    }

    @Test
    void testInvalidJwt(){
        GetTransactionResponse resp = GetTransactionResponse.builder()
                .status(ResponseEnum.FAILURE.getMessage())
                .responseDescription("Invalid JWT")
                .responseCode(ResponseEnum.FAILURE.getCode())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        assertEquals (resp
                ,transactionService.getTransaction("bearer e"+jwt,"SEP", "DEBIT"));
    }


}
