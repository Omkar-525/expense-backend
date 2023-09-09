package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.entity.User;

import java.util.List;

public interface UserService {
    List<User> searchUsers(String authorizationHeader, String query);
}
