package com.omkar.expensetracker.infra.model;

import java.util.Map;

public class MonthlyTransactions {
    Map<String, Map<String, Integer>> monthlyTransactions;

    public MonthlyTransactions(Map<String, Map<String, Integer>> monthlyTransactions) {
        this.monthlyTransactions = monthlyTransactions;
    }

    public Map<String, Map<String, Integer>> getMonthlyTransactions() {
        return monthlyTransactions;
    }

    public void setMonthlyTransactions(Map<String, Map<String, Integer>> monthlyTransactions) {
        this.monthlyTransactions = monthlyTransactions;
    }
}