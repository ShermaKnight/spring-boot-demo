package org.example.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DroolsRule {

    private Long ruleId;
    private String kieBaseName;

    private String kiePackageName;

    private String ruleContent;

    private Date createdTime;

    private Date updateTime;

    public void validate() {
        if (this.ruleId == null || isBlank(kieBaseName) || isBlank(kiePackageName) || isBlank(ruleContent)) {
            throw new RuntimeException("parameter invalid");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }
}
