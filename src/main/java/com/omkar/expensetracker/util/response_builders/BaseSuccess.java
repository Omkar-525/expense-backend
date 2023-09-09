package com.omkar.expensetracker.util.response_builders;

import com.omkar.expensetracker.infra.model.BaseResponse;
import com.omkar.expensetracker.util.enums.ResponseEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BaseSuccess {

    public BaseResponse baseSuccessResponse (String description){
        var baseResponse = new BaseResponse();
        baseResponse.setHttpStatus(HttpStatus.OK);
        baseResponse.setResponseCode(ResponseEnum.SUCCESS.getCode());
        baseResponse.setStatus(ResponseEnum.SUCCESS.getMessage());
        baseResponse.setResponseDescription(description);
        return baseResponse;
    }
}
