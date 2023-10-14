package com.omkar.expensetracker.business.controller;

import com.omkar.expensetracker.business.service.UserService;
import com.omkar.expensetracker.infra.entity.User;
import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.infra.model.request.ChangePasswordRequest;
import com.omkar.expensetracker.infra.model.response.DataResponse;
import com.omkar.expensetracker.infra.model.response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<DataResponse<List<User>>> searchUsers(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                                @RequestParam String query) {
        List<User> users = userService.searchUsers(authorizationHeader, query);
        return ResponseEntity.ok(new DataResponse<>(HttpStatus.OK, "Success", "200", "Users fetched successfully", users));
    }
    @GetMapping("/profile")
    public ProfileResponse getUserProfile(@RequestHeader(value = "Authorization") String authorizationHeader){
        return userService.getProfile(authorizationHeader);
    }
    @PostMapping("/changepassword")
    public BaseResponse changePassword(@RequestHeader(value = "Authorization") String authorizationHeader,
                                       @RequestBody ChangePasswordRequest request){
        return userService.changePassword(authorizationHeader,request);
    }
}
