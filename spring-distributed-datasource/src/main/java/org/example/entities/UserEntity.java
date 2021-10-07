package org.example.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tbl_user")
@ToString
public class UserEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userName;
    private String address;
    private BigDecimal salary;
    private Date createTime;
    private Date updateTime;
}