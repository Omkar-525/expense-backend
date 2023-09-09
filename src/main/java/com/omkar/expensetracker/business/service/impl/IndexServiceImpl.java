package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.IndexService;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.LoginRequest;
import com.omkar.expensetracker.infra.model.request.RegisterRequest;
import com.omkar.expensetracker.infra.model.response.LoginResponse;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.security.jwt.JwtUtil;
import com.omkar.expensetracker.util.response_builders.BaseFailure;
import com.omkar.expensetracker.util.response_builders.BaseSuccess;
import com.omkar.expensetracker.util.response_builders.failure.FailureResponseBuilder;
import com.omkar.expensetracker.util.response_builders.success.SuccessResponseBuilder;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuccessResponseBuilder successResponse;

    @Autowired
    private BaseSuccess baseSuccessBuilder;

    @Autowired
    private BaseFailure baseFailureBuilder;

    @Autowired
    private FailureResponseBuilder failureResponse;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login Request received [{}]", request);
        LoginResponse response;
        // fetch user using email
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(user.isPresent()){
            User userFetched = user.get();
            // check if the password is correct
            if(passwordEncoder.matches(request.getPassword(),userFetched.getPassword())){
                // success
                // generate JWT
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
                Map<String, Object> claims = generateClaims(userFetched);
                String jwt = jwtUtil.generateToken(userDetails,claims);

                response= successResponse.loginSuccess(jwt, user.get());

            } else {
                response =  failureResponse.loginFailed("Invalid Credentials");
            }
        } else {
            response = failureResponse.loginFailed("User Not Present");
        }
        log.info("Login Response {}", response.toString());
        return response;
    }

    @Override
    public BaseResponse register(RegisterRequest request) {
        // email is already present
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if(!userOptional.isPresent()){
            // build User
            User user = User.builder()
                    .email(request.getEmail())
                    .name(request.getName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .active(true)
                    .role("User")
                    .splits(new HashSet<>())
                    .budget(new HashSet<>())
                    .transactions(new HashSet<>())
                    .build();
            //save user
            userRepository.save(user);

            return baseSuccessBuilder.baseSuccessResponse("User Created");
        }
        else{
            return baseFailureBuilder.baseFailResponse("User already Exists");
        }

    }

    public static Map<String, Object> generateClaims( User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return claims;
    }
}
