package org.example.service;

import org.example.domain.dto.DroolsRule;

import java.util.List;

public interface DroolsRuleService {

    List<DroolsRule> findAll();

    void addDroolsRule(DroolsRule droolsRule);

    void updateDroolsRule(DroolsRule droolsRule);

    void deleteDroolsRule(Long ruleId, String ruleName);
}
