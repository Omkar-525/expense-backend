package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.ChangePasswordRequest;
import com.omkar.expensetracker.infra.model.response.ProfileResponse;

import java.util.List;

public interface UserService {
    public  ProfileResponse getProfile(String authorizationHeader);

    List<User> searchUsers(String authorizationHeader, String query);

    BaseResponse changePassword(String authorizationHeader, ChangePasswordRequest request);
}
