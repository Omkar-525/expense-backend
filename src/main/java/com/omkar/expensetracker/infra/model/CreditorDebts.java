package com.omkar.expensetracker.infra.model;

import com.omkar.expensetracker.infra.entity.Debt;
import com.omkar.expensetracker.infra.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditorDebts {
    private User creditor;
    private List<Debt> debts;
}
