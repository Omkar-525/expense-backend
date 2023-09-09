package com.omkar.expensetracker.infra.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.omkar.expensetracker.util.constants.JsonConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse implements Serializable {

    private HttpStatus httpStatus;

    @JsonProperty(JsonConstants.BASE_RESPONSE_STATUS)
    private String status;

    @JsonProperty(JsonConstants.BASE_RESPONSE_CODE)
    private String responseCode;

    @JsonProperty(JsonConstants.BASE_RESPONSE_DESCRIPTION)
    private String responseDescription;
}
