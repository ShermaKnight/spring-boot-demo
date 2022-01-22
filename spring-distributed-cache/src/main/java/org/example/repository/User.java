package org.example.repository;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class User implements Serializable {

    private Integer id;
    private String userName;
    private String nickName;
    private Double salary;
    private Date birthday;
    private List<Address> addresses;
}
