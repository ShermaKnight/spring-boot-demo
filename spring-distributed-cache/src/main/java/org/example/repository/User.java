package org.example.repository;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User {

    private Integer id;
    private String userName;
    private String nickName;
    private Float salary;
    private Date birthday;
    private List<Address> addresses;
}
