package com.omkar.expensetracker.util;

import com.omkar.expensetracker.infra.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class DebtCalculator {

    public List<Debt> calculateDebts(Split split) {
        List<Debt> debts = new ArrayList<>();

        // Calculate total amount in the split
        int totalAmount = split.getTransactions().stream().mapToInt(Transaction::getAmount).sum();

        int numberOfUsers = split.getUsers().size();
        int perPersonAmount = totalAmount / numberOfUsers;

        // Map to store individual user contributions
        Map<User, Integer> userContributions = new HashMap<>();

        // Populate the userContributions map
        for (Transaction transaction : split.getTransactions()) {
            userContributions.put(transaction.getUser(),
                    userContributions.getOrDefault(transaction.getUser(), 0) + transaction.getAmount());
        }

        // Maps to store owed and owing users
        Map<User, Integer> owedUsers = new HashMap<>();
        Map<User, Integer> owingUsers = new HashMap<>();

        for (User user : split.getUsers()) {
            int contributedAmount = userContributions.getOrDefault(user, 0);
            int difference = contributedAmount - perPersonAmount;
            if (difference > 0) {
                owedUsers.put(user, difference);
            } else if (difference < 0) {
                owingUsers.put(user, -difference); // storing as positive value
            }
        }

        // Settling the debts
        for (Map.Entry<User, Integer> owedEntry : owedUsers.entrySet()) {
            User owedUser = owedEntry.getKey();
            int owedAmount = owedEntry.getValue();

            Iterator<Map.Entry<User, Integer>> owingIterator = owingUsers.entrySet().iterator();
            while (owedAmount > 0 && owingIterator.hasNext()) {
                Map.Entry<User, Integer> owingEntry = owingIterator.next();
                User owingUser = owingEntry.getKey();
                int owingAmount = owingEntry.getValue();

                int settledAmount = Math.min(owedAmount, owingAmount);

                // Create debt object
                Debt debt = new Debt();
                debt.setCreditor(owedUser);
                debt.setDebtor(owingUser);
                debt.setAmount(BigDecimal.valueOf(settledAmount));

                debts.add(debt);

                // Reduce the amounts
                owedAmount -= settledAmount;
                owingAmount -= settledAmount;

                if (owingAmount == 0) {
                    owingIterator.remove();
                } else {
                    owingEntry.setValue(owingAmount);
                }
            }
        }

        return debts;
    }
}
