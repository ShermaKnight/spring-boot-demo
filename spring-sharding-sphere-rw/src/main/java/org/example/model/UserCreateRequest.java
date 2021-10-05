package org.example.model;

import lombok.Data;
import org.example.entities.AddressEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserCreateRequest {

    @NotEmpty(message = "用户名不允许为空")
    private String userName;

    @NotNull(message = "性别不允许为空")
    private Byte gender;

    @NotNull(message = "薪资不允许为空")
    private BigDecimal salary;

    @Valid
    private List<AddressCreateRequest> addresses = new ArrayList<>();
}
