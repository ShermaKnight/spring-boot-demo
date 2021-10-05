package org.example.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tbl_course")
public class CourseEntity implements Serializable {

    private Long id;
    private String courseName;
    private BigDecimal price;
    private Date createTime;
    private Date updateTime;
}
