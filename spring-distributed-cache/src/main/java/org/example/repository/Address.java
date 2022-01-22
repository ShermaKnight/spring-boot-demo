package org.example.repository;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    private Integer id;
    private Integer userId;
    private String name;
    private String country;
    private String city;
    private String detail;
    private boolean collect = false;
}
