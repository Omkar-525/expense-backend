package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.model.response.GetDashboardResponse;

public interface DashboardService {
    GetDashboardResponse dashboardInfo(String authorizationHeader);
}
