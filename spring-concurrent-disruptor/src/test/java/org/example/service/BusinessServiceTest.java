package org.example.service;

import cn.hutool.core.util.RandomUtil;
import org.example.domain.dto.Athlete;
import org.example.domain.dto.Referee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("BusinessService测试用例")
public class BusinessServiceTest {

    @Resource
    private BusinessService businessService;

    @Test
    @DisplayName("运行测试")
    public void runningTest() {
        List<Athlete> athletes = new ArrayList<Athlete>() {
            {
                IntStream.range(0, 10000).boxed().forEach(i -> add(new Athlete(RandomUtil.randomStringUpper(10))));
            }
        };
        Referee referee = new Referee("A");
        businessService.running(athletes, referee);
    }
}
