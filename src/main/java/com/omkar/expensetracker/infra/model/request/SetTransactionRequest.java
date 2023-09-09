package com.omkar.expensetracker.infra.model.request;


import com.omkar.expensetracker.infra.model.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetTransactionRequest extends BaseRequest {

    public String date;

    public String type;

    public Integer amount;

    public String category;


}