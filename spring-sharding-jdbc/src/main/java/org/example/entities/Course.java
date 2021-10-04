package org.example.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Course {

    private Long id;
    private String courseName;
    private BigDecimal price;
    private Date createTime;
    private Date updateTime;
}
