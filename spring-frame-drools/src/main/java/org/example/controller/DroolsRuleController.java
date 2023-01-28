package org.example.controller;

import org.example.domain.dto.CommonResult;
import org.example.domain.dto.DroolsRule;
import org.example.service.DroolsManager;
import org.example.service.DroolsRuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drools/rule")
public class DroolsRuleController {

    @Resource
    private DroolsRuleService droolsRuleService;

    @Resource
    private DroolsManager droolsManager;

    @GetMapping("/findAll")
    public List<DroolsRule> findAll() {
        return droolsRuleService.findAll();
    }

    @PostMapping("/add")
    public CommonResult addRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.addDroolsRule(droolsRule);
        return new CommonResult(200, "success");
    }

    @PostMapping("/update")
    public CommonResult updateRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.updateDroolsRule(droolsRule);
        return new CommonResult(200, "success");
    }

    @PostMapping("/deleteRule")
    public CommonResult deleteRule(Long ruleId, String ruleName) {
        droolsRuleService.deleteDroolsRule(ruleId, ruleName);
        return new CommonResult(200, "success");
    }

    @GetMapping("/fireRule")
    public CommonResult fireRule(String kieBaseName, Integer param) {
        return new CommonResult(200, "success", droolsManager.fireRule(kieBaseName, param));
    }
}
