package org.example.mapstruct;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String userName;
    private String status;
    private String birthday;
}
