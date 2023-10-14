package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.TransactionService;
import com.omkar.expensetracker.infra.entity.Category;
import com.omkar.expensetracker.infra.entity.Transaction;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.GetTransactionRequest;
import com.omkar.expensetracker.infra.model.request.SetTransactionRequest;
import com.omkar.expensetracker.infra.model.response.GetTransactionResponse;
import com.omkar.expensetracker.infra.repository.CategoryRepository;
import com.omkar.expensetracker.infra.repository.TransactionRepository;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.security.CustomUserDetailsService;
import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.AppUtil;
import com.omkar.expensetracker.util.response_builders.BaseFailure;
import com.omkar.expensetracker.util.response_builders.BaseSuccess;
import com.omkar.expensetracker.util.response_builders.failure.FailureResponseBuilder;
import com.omkar.expensetracker.util.response_builders.success.SuccessResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

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
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BaseSuccess baseSuccess;

    @Autowired
    private BaseFailure baseFailure;

    @Override
    public GetTransactionResponse getTransaction(String authorizationHeader, String month, String type) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    List<Transaction> transactions;
                    if (Objects.nonNull(type)) {
                        transactions = transactionRepository.findByUserAndDateContainingAndType(user.get()
                                , month, type);
                    } else {
                        transactions = transactionRepository.findByUserAndDateContaining(user.get(), month);
                    }

                    return successResponse.getTransaction(transactions);
                }
                return failureResponse.getTransaction("User Not Present");
            } catch (Exception ex) {
                return failureResponse.getTransaction("Something went wrong");
            }
        }
        return failureResponse.getTransaction("Invalid JWT");
    }

    @Override
    public BaseResponse setTransaction(String authorizationHeader, SetTransactionRequest request) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                Optional<Category> category = categoryRepository.findByName(request.getCategory());
                Category cat;
                if (category.isEmpty()) {
                    Category c = Category.builder().name(request.getCategory()).build();
                    cat = categoryRepository.save(c);
                } else {
                    cat = category.get();
                }
                if (user.isPresent()) {
                    Transaction transaction = Transaction.builder()
                            .type(request.getType())
                            .amount(request.getAmount())
                            .date(request.getDate())
                            .category(cat)
                            .description(request.getDescription())
                            .user(user.get())
                            .build();
                    transactionRepository.save(transaction);
                    return baseSuccess.baseSuccessResponse("Transaction Created");
                }
                return baseFailure.baseFailResponse("User not present");

            } catch (Exception ex) {
                return baseFailure.baseFailResponse("Something went wrong");

            }


        }
        return baseFailure.baseFailResponse("Invalid JWT");

    }

    @Override
    public BaseResponse deleteTransaction(String authorizationHeader, Long id) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    Optional<Transaction> transaction = transactionRepository.findByIdAndUser(id, user.get());
                    if (transaction.isPresent()) {
                        transactionRepository.delete(transaction.get());
                        return baseSuccess.baseSuccessResponse("Transaction deleted");
                    } else {
                        return baseFailure.baseFailResponse("You don't have permission to delete this transaction");
                    }
                } else {
                    return baseFailure.baseFailResponse("User is not present");
                }
            } catch (Exception ex) {
                return baseFailure.baseFailResponse("Something went wrong");

            }
        }
        return baseFailure.baseFailResponse("Invalid JWT");
    }

    @Override
    public GetTransactionResponse getAllTransaction(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    List<Transaction> transactions = transactionRepository.findAllByUser(user.get());
                    return successResponse.getTransaction(transactions);
                }
                return failureResponse.getTransaction("User Not Present");
            } catch (Exception ex) {
                return failureResponse.getTransaction("Something went wrong");
            }
        }
        return failureResponse.getTransaction("Invalid JWT");
    }
}