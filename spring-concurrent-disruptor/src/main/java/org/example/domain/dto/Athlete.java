package org.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Athlete {

    private String name;
    private Long time = -1l;
    private Double score = 0d;

    public Athlete(String name) {
        this.name = name;
    }
}
