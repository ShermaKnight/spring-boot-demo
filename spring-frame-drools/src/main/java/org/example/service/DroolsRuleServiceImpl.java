package org.example.service;

import org.example.domain.dto.DroolsRule;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DroolsRuleServiceImpl implements DroolsRuleService {

    @Resource
    private DroolsManager droolsManager;

    private Map<Long, DroolsRule> droolsRuleCache = new HashMap<>(16);

    @Override
    public List<DroolsRule> findAll() {
        return new ArrayList<>(droolsRuleCache.values());
    }

    @Override
    public void addDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setCreatedTime(new Date());
        droolsRuleCache.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void updateDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setUpdateTime(new Date());
        droolsRuleCache.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void deleteDroolsRule(Long ruleId, String ruleName) {
        DroolsRule droolsRule = droolsRuleCache.get(ruleId);
        if (null != droolsRule) {
            droolsRuleCache.remove(ruleId);
            droolsManager.deleteDroolsRule(droolsRule.getKieBaseName(), droolsRule.getKiePackageName(), ruleName);
        }
    }
}
