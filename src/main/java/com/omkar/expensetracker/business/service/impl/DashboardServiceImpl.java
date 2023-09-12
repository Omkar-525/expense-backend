package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.DashboardService;
import com.omkar.expensetracker.infra.entity.Split;
import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.response.GetDashboardResponse;
import com.omkar.expensetracker.infra.repository.SplitRepository;
import com.omkar.expensetracker.infra.repository.TransactionRepository;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.security.CustomUserDetailsService;
import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.AppUtil;
import com.omkar.expensetracker.util.response_builders.failure.FailureResponseBuilder;
import com.omkar.expensetracker.util.response_builders.success.SuccessResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SuccessResponseBuilder successResponse;

    @Autowired
    private FailureResponseBuilder failureResponse;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SplitRepository splitRepository;

    @Override
    public GetDashboardResponse dashboardInfo(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String[] m = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        List<String> months = Arrays.asList(m);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    List<Transaction> transactions = transactionRepository.findAllByUser(user.get());
                    List<Transaction> thisMonthsTransaction = transactionRepository.findByUserAndDateContainingAndType(user.get(),
                            appUtil.getMonth(), "Debit");

                    Integer totalExpense = 0 ;
                    double totalWeOwe = 0 ;
                    double totalOwed = 0;
                    for(Transaction transaction: thisMonthsTransaction){
                        totalExpense += transaction.getAmount();
                    }
                    List<Split> splits = user.get().getSplits().stream()
                            .filter(split -> !split.getIsFinalized()).toList();
                    for(Split split: splits){
                        List<Transaction> splTrans = split.getTransactions().stream().toList();
                        for(Transaction t :splTrans) {
                            User creditor = t.getUser();
                            double amount = t.getAmount();
                            double share = amount / split.getUsers().size();
                            if (user.get().equals(creditor)) {
                                totalWeOwe += share*(split.getUsers().size()-1);
                            } else {
                                totalOwed += share;
                            }
                        }
                    }
                    return successResponse.dashboardInfo(getMonthlyTransactions(transactions), totalExpense,totalWeOwe,totalOwed);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return failureResponse.dashboardInfo("Something went wrong");

            }
        }
        return failureResponse.dashboardInfo("Invalid JWT");
    }

    private static  Map<String, Map<String,Integer>> getMonthlyTransactions(List<Transaction> transactions){
        Map<String, Map<String, Integer>> monthlyTransactions = new HashMap<>();
        transactions.forEach(transaction -> {
            String date = transaction.getDate();
            String mon = date.substring(3,6);
            if(monthlyTransactions.containsKey(mon)){
                Map<String, Integer> typeAndAmount = monthlyTransactions.get(mon);
                if(typeAndAmount.containsKey(transaction.getType())){
                    typeAndAmount.put(transaction.getType(), typeAndAmount.get(transaction.getType())+transaction.getAmount());
                }else {
                    typeAndAmount.put(transaction.getType(), transaction.getAmount());
                }
                monthlyTransactions.put(mon, typeAndAmount);
            } else {
                Map<String, Integer> typeAndAmount = new HashMap<>();
                typeAndAmount.put(transaction.getType(), transaction.getAmount());
                monthlyTransactions.put(mon, typeAndAmount);
            }
        });
        return monthlyTransactions;
    }
}
