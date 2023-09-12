package com.omkar.expensetracker.infra.model;

public class TotalExpense {
    public Integer totalExpense;
    public double totalOwed;
    public double totalWeOwe;

    public TotalExpense() {
    }

    public TotalExpense(Integer totalExpense, double totalOwed, double totalWeOwe) {
        this.totalExpense = totalExpense;
        this.totalOwed = totalOwed;
        this.totalWeOwe = totalWeOwe;
    }

    public Integer getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Integer totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getTotalOwed() {
        return totalOwed;
    }

    public void setTotalOwed(double totalOwed) {
        this.totalOwed = totalOwed;
    }

    public double getTotalWeOwe() {
        return totalWeOwe;
    }

    public void setTotalWeOwe(double totalWeOwe) {
        this.totalWeOwe = totalWeOwe;
    }
}


