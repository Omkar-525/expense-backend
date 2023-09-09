package com.omkar.expensetracker.infra.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.omkar.expensetracker.infra.model.BaseResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class DataResponse<T> extends BaseResponse {

    @JsonProperty("data")
    private T data;

    public DataResponse(HttpStatus httpStatus, String status, String responseCode, String responseDescription, T data) {
        super(httpStatus, status, responseCode, responseDescription);
        this.data = data;
    }
}
