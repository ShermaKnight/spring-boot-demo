package org.example.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRequest {

    @NotNull(message = "用户ID不允许为空")
    private Long id;

    @NotEmpty(message = "用户名不允许为空")
    private String userName;
}
