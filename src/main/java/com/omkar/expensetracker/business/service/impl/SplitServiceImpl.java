package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.SplitService;
import com.omkar.expensetracker.infra.entity.Split;
import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.entity.Debt;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.exception.InvalidJwtException;
import com.omkar.expensetracker.infra.model.request.CreateSplitRequest;
import com.omkar.expensetracker.infra.repository.SplitRepository;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.infra.repository.TransactionRepository;

import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.DebtCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SplitServiceImpl implements SplitService {

    @Autowired
    private SplitRepository splitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DebtCalculator debtCalculator;

    private User validateAndExtractUserFromJwt(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not present in the database"));
        } else {
            throw new SecurityException("Invalid JWT token");
        }
    }

    @Override
    @Transactional
    public Split createSplit(String authorizationHeader, CreateSplitRequest request) {
        User loggedInUser = validateAndExtractUserFromJwt(authorizationHeader);
        // You can use loggedInUser for any logic that involves the user initiating the request.

        List<User> users =  userRepository.findAllById(request.getUserIds());
        if (users.size() != request.getUserIds().size()) {
            throw new IllegalArgumentException("Some user IDs are invalid.");
        }

        Split split = new Split();
        split.setUsers(new HashSet<>(users));
        split.setTitle(request.getTitle());
        return splitRepository.save(split);
    }

    @Override
    public Split getSplit(String authorizationHeader, Long splitId) {
        User loggedInUser = validateAndExtractUserFromJwt(authorizationHeader);
        // Logic involving loggedInUser (e.g., check if user is part of the split)

        return splitRepository.findById(splitId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Split ID."));
    }


    @Override
    public Transaction addTransactionToSplit(String authorizationHeader, Long splitId, Transaction transaction) {
        String email = extractEmailFromJwt(authorizationHeader);
        User user = getUserByEmail(email);

        Split split = splitRepository.findById(splitId).orElseThrow(() -> new EntityNotFoundException("Split not found"));

        transaction.setUser(user);
        transaction.setSplit(split);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsOfSplit(String authorizationHeader, Long splitId) {
        validateUserFromJwt(authorizationHeader);
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new EntityNotFoundException("Split not found"));
        return new ArrayList<>(split.getTransactions());
    }

    @Override
    public List<Debt> getDebtSummary(String authorizationHeader, Long splitId) {
        validateUserFromJwt(authorizationHeader);
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new EntityNotFoundException("Split not found"));

        // Assuming you have a utility method to calculate debts for a given split
        return debtCalculator.calculateDebts(split);
    }

    @Override
    public Split finalizeSplit(String authorizationHeader, Long splitId) {
        validateUserFromJwt(authorizationHeader);
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new EntityNotFoundException("Split not found"));

        // Assuming Split entity has a 'finalized' field
        split.setIsFinalized(true);
        return splitRepository.save(split);
    }

    @Override
    public List<Split> getAllSplitsForUser(String authorizationHeader) {
        User loggedInUser = validateAndExtractUserFromJwt(authorizationHeader);

        return new ArrayList<>(loggedInUser.getSplits());
    }


    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private String extractEmailFromJwt(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        return jwtUtil.extractUsername(jwt);
    }

    private void validateUserFromJwt(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtUtil.validateToken(jwt, userDetails)) {
            throw new InvalidJwtException("Invalid JWT");
        }
    }
}
