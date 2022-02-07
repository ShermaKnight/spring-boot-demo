package org.example.domain.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User {

    private Integer id;
    private String userName;
    private Double salary;
    private Date birthday;
    private List<Address> addresses;
    private Date createTime;
}
