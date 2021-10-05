package org.example.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tbl_user")
public class UserEntity {

    private Long id;
    private String userName;
    private Byte gender;
    private BigDecimal salary;
    private Date createTime;
    private Date updateTime;
}
