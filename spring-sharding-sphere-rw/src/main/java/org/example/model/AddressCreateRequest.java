package org.example.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddressCreateRequest {

    @NotEmpty(message = "收件人不允许为空")
    private String receiver;

    @NotEmpty(message = "地址不允许为空")
    private String address;
}
