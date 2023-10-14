package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.UserService;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.exception.InvalidJwtException;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.ChangePasswordRequest;
import com.omkar.expensetracker.infra.model.response.ProfileResponse;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.response_builders.BaseFailure;
import com.omkar.expensetracker.util.response_builders.BaseSuccess;
import com.omkar.expensetracker.util.response_builders.failure.FailureResponseBuilder;
import com.omkar.expensetracker.util.response_builders.success.SuccessResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    FailureResponseBuilder failureResponse;

    @Autowired
    private SuccessResponseBuilder successResponse;

    @Autowired
    private BaseSuccess baseSuccess;

    @Autowired
    private BaseFailure baseFailure;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponse getProfile(String authorizationHeader) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    return successResponse.getUser( user.get());

                } else {
                    return failureResponse.getUser("user not present");
                }
            } catch (Exception ex) {
                return failureResponse.getUser("Something went wrong");
            }
        }
        return failureResponse.getUser("Invalid JWT");
    }

    @Override
    public List<User> searchUsers(String authorizationHeader, String query) {
        validateUserFromJwt(authorizationHeader);
        return userRepository.findByNameContainingIgnoreCase(query);
    }

    @Override
    public BaseResponse changePassword(String authorizationHeader, ChangePasswordRequest request) {
        String jwt = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
            try {
                Optional<User> user = userRepository.findByEmail(email);
                if (user.isPresent()) {
                    if(passwordEncoder.matches(request.getOldPassword(),user.get().getPassword())){
                        User u = user.get();
                        u.setPassword(passwordEncoder.encode(request.newPassword));
                        userRepository.save(u);
                        return baseSuccess.baseSuccessResponse("Password changed");
                    }else{
                        return baseFailure.baseFailResponse("Invalid password");
                    }
                }
                return baseFailure.baseFailResponse("Invalid user");
            } catch (Exception ex) {
                ex.printStackTrace();
                return baseFailure.baseFailResponse("something went wrong");
            }
        }
        return baseFailure.baseFailResponse("Invalid JWt");
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
