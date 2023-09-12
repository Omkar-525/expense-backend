package com.omkar.expensetracker.business.controller;

import com.omkar.expensetracker.business.service.DashboardService;
import com.omkar.expensetracker.infra.model.response.GetDashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public GetDashboardResponse dashboardInfo(@RequestHeader(value = "Authorization")
                                                  String authorizationHeader){
        return dashboardService.dashboardInfo(authorizationHeader);
    }

}
