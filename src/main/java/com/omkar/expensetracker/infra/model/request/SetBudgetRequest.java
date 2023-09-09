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
public class SetBudgetRequest extends BaseRequest {

    public Long budget;

    public String month;
}
