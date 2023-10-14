package com.omkar.expensetracker.infra.model.response;

import com.omkar.expensetracker.infra.model.BaseResponse;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse extends BaseResponse {

    private String name;

    private String email;

    @Builder
    public ProfileResponse(HttpStatus httpStatus, String status, String responseCode, String responseDescription, String name, String email) {
        super(httpStatus, status, responseCode, responseDescription);
        this.name = name;
        this.email = email;
    }
}
