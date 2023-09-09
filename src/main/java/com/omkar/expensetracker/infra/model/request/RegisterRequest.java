package com.omkar.expensetracker.infra.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.omkar.expensetracker.infra.model.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends BaseRequest {


    private String name;

    private String email;

    private String password;
}
