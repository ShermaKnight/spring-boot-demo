package org.example.mapstruct;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {

    private String name;
    private Integer status;
    private Date birthday;
}
