package org.example.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Order {

    private int price;
    private int score;
    private Date bookingDate;
}
