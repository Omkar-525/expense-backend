package com.omkar.expensetracker.infra.model.request;

import com.omkar.expensetracker.infra.model.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserToSplitRequest extends BaseRequest {

    List<Long> userIds;
}
