package org.example.model;

import lombok.Data;
import org.example.entities.OrderEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BusinessResponse {

    private Long id;
    private String userName;
    private String address;
    private BigDecimal salary;
    private Date createTime;
    private Date updateTime;
    private List<OrderEntity> orders = new ArrayList<>();
}
