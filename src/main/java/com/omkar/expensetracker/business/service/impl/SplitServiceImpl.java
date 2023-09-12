package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.SplitService;
import com.omkar.expensetracker.infra.entity.*;
import com.omkar.expensetracker.infra.exception.InvalidJwtException;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.CreditorDebts;
import com.omkar.expensetracker.infra.model.request.CreateSplitRequest;
import com.omkar.expensetracker.infra.repository.SettlementRepository;
import com.omkar.expensetracker.infra.repository.SplitRepository;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.infra.repository.TransactionRepository;

import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.DebtCalculator;
import com.omkar.expensetracker.util.response_builders.BaseFailure;
import com.omkar.expensetracker.util.response_builders.BaseSuccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

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
    private SettlementRepository settlementRepository;


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BaseFailure baseFailure;

    @Autowired
    private BaseSuccess baseSuccess;

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
        split.setOwner(loggedInUser);
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
    public List<CreditorDebts> getDebtSummary(String authorizationHeader, Long splitId) {
        validateUserFromJwt(authorizationHeader);
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new EntityNotFoundException("Split not found"));

        Map<User, Map<User, BigDecimal>> groupedDebts = new HashMap<>();

        // For each transaction, calculate the individual debts
        for (Transaction transaction : split.getTransactions()) {
            User creditor = transaction.getUser();
            double amount = transaction.getAmount();
            double share = amount / split.getUsers().size();

            groupedDebts.putIfAbsent(creditor, new HashMap<>());

            for (User user : split.getUsers()) {
                if (!user.equals(creditor)) {
                    BigDecimal debtorAmount = groupedDebts.get(creditor).getOrDefault(user, BigDecimal.ZERO);
                    debtorAmount = debtorAmount.add(BigDecimal.valueOf(share));
                    groupedDebts.get(creditor).put(user, debtorAmount);
                }
            }
        }
        List<CreditorDebts> results = new ArrayList<>();
        for (Map.Entry<User, Map<User, BigDecimal>> entry : groupedDebts.entrySet()) {
            User creditor = entry.getKey();
            CreditorDebts creditorDebts = new CreditorDebts();
            creditorDebts.setCreditor(creditor);

            List<Debt> debtsForCreditor = new ArrayList<>();
            for (Map.Entry<User, BigDecimal> debtorEntry : entry.getValue().entrySet()) {
                Debt debt = new Debt();
                debt.setCreditor(entry.getKey());
                debt.setDebtor(debtorEntry.getKey());
                debt.setAmount(debtorEntry.getValue());
                debtsForCreditor.add(debt);
            }

            creditorDebts.setDebts(debtsForCreditor);
            results.add(creditorDebts);
        }


        return results;
    }

    @Override
    @Transactional
    public Split finalizeSplit(String authorizationHeader, Long splitId) {
        validateUserFromJwt(authorizationHeader);
        Split split = splitRepository.findById(splitId)
                .orElseThrow(() -> new EntityNotFoundException("Split not found"));

        User loggedInUser = validateAndExtractUserFromJwt(authorizationHeader);

        if (!loggedInUser.equals(split.getOwner())) {
            throw new IllegalArgumentException("Unauthorized to finalize the split");
        }

        // Calculating and storing settlements
        Map<User, Map<User, BigDecimal>> groupedDebts = new HashMap<>();

        for (Transaction transaction : split.getTransactions()) {
            User creditor = transaction.getUser();
            double amount = transaction.getAmount();
            double share = amount / split.getUsers().size();

            for (User user : split.getUsers()) {
                if (!user.equals(creditor)) {
                    groupedDebts.putIfAbsent(creditor, new HashMap<>());
                    BigDecimal debtorAmount = groupedDebts.get(creditor).getOrDefault(user, BigDecimal.ZERO);
                    debtorAmount = debtorAmount.add(BigDecimal.valueOf(share));
                    groupedDebts.get(creditor).put(user, debtorAmount);
                }
            }
        }

        // Save settlements in a batch
        List<Settlement> settlements = new ArrayList<>();
        for (Map.Entry<User, Map<User, BigDecimal>> entry : groupedDebts.entrySet()) {
            User creditor = entry.getKey();

            for (Map.Entry<User, BigDecimal> debtorEntry : entry.getValue().entrySet()) {
                // Create a corresponding Settlement entry:
                Settlement settlement = new Settlement();
                settlement.setCreditor(creditor);
                settlement.setDebtor(debtorEntry.getKey());
                settlement.setAmount(debtorEntry.getValue());
                settlements.add(settlement);
            }
        }
        settlementRepository.saveAll(settlements);  // Assuming this method exists

        // Finalizing split
        split.setIsFinalized(true);
        return splitRepository.save(split);
    }


    @Override
    public List<Split> getAllSplitsForUser(String authorizationHeader) {
        User loggedInUser = validateAndExtractUserFromJwt(authorizationHeader);

        return new ArrayList<>(loggedInUser.getSplits());
    }

    @Override
    public ResponseEntity<BaseResponse> addUsersToSplit(String authorizationHeader, Long splitId, List<Long> userIds) {
        User loggedInUser = validateAndExtractUserFromJwt(authorizationHeader);
        Optional<Split> splitOptional = splitRepository.findById(splitId);
        if (!splitOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseFailure.baseFailResponse("Invalid Split Id"));
        }
        if(!splitOptional.get().getUsers().contains(loggedInUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseFailure.baseFailResponse("Unauthorized to chaneg the split"));
        }
        Split split = splitOptional.get();
        List<User> usersToAdd = userRepository.findAllById(userIds);

        if (usersToAdd.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseFailure.baseFailResponse("In valid User ids"));
        }

        split.getUsers().addAll(usersToAdd);
        splitRepository.save(split);
        usersToAdd.forEach(user -> user.getSplits().add(split));
        userRepository.saveAll(usersToAdd);

        return ResponseEntity.status(HttpStatus.OK).body(baseSuccess.baseSuccessResponse("Users added successfully"));

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
