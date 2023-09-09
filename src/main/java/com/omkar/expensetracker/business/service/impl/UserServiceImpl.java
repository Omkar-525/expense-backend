package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.UserService;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.exception.InvalidJwtException;
import com.omkar.expensetracker.infra.repository.UserRepository;
import com.omkar.expensetracker.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public List<User> searchUsers(String authorizationHeader, String query) {
        validateUserFromJwt(authorizationHeader);
        return userRepository.findByNameContainingIgnoreCase(query);
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
