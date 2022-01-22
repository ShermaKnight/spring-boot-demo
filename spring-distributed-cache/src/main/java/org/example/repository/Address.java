package org.example.repository;

import lombok.Data;

@Data
public class Address {

    private Integer id;
    private Integer userId;
    private String name;
    private String country;
    private String city;
    private String detail;
    private boolean collect = false;
}
