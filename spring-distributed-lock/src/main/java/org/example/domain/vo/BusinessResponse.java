package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponse {

    private String path;
    private Date current;

    public BusinessResponse(Date current) {
        this.current = current;
    }
}
