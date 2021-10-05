package org.example.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tbl_address")
public class AddressEntity {

    private Long id;
    private Long userId;
    private String receiver;
    private String address;
    private Date createTime;
    private Date updateTime;
}
