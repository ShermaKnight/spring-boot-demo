package org.example;


import cn.hutool.core.util.RandomUtil;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.example.domain.dto.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("Drools测试用例")
public class OrderRules {

    @Resource
    private KieSession kieSession;

    @Resource
    private KieContainer kieContainer;

    @Test
    @DisplayName("新增积分")
    public void addScoreTest() {
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setPrice(RandomUtil.randomInt(1, 1001));
            kieSession.insert(order);
            int rules = kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("order_score_"));
            System.out.println("当前价格为: " + order.getPrice() + ", 触发了" + rules + "条规则, 用户享受额外增加积分: " + order.getScore());
        }
    }
}
