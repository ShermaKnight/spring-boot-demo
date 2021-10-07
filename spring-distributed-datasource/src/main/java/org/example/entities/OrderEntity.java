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
@TableName("tbl_order")
@ToString
public class OrderEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String url;
    private BigDecimal price;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}