package org.example.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderEntity implements Serializable {

    private Long id;
    private Long userId;
    private String courseName;
    private BigDecimal price;
    private Date createTime;
    private Date updateTime;
}
