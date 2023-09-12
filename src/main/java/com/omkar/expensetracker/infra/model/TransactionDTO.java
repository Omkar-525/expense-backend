package com.omkar.expensetracker.infra.model;

import com.omkar.expensetracker.infra.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO implements Serializable {

    public Long id;
    public String date; // 12-AUG-2023
    public String type;
    public String category;
    public Integer amount;
    public String description;

    public TransactionDTO(Transaction transaction) {
        this.id=transaction.getId();
        this.amount= transaction.getAmount();
        this.category= transaction.getCategory().getName();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
    }
}
