package com.omkar.expensetracker.infra.model.request;

import com.omkar.expensetracker.infra.model.BaseRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest extends BaseRequest {

    private String email;

    private String password;

}
