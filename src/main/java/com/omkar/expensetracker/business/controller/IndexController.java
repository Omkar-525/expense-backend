package com.omkar.expensetracker.business.controller;

import com.omkar.expensetracker.business.service.IndexService;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.LoginRequest;
import com.omkar.expensetracker.infra.model.request.RegisterRequest;
import com.omkar.expensetracker.infra.model.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return indexService.login(request);
    }

    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterRequest request){
        return indexService.register(request);
    }
}
