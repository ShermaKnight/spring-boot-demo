package org.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.dao.OrderMapper;
import org.example.entities.OrderEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShardingOrderTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void insertBatch() {
        IntStream.range(0, 100).forEach(i -> {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setCourseName("Nothing");
            orderEntity.setUserId(new Long(i));
            orderEntity.setPrice(new BigDecimal(198.00 + i));
            orderEntity.setCreateTime(new Date());
            orderEntity.setUpdateTime(new Date());
            orderMapper.insert(orderEntity);
        });
    }

    @Test
    public void selectByCondition() {
        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(OrderEntity::getPrice, 230);
        Integer count = orderMapper.selectCount(queryWrapper);
        Assert.assertEquals(new Integer(68), count);
    }

    @Test
    public void selectByOrder() {
        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(OrderEntity::getPrice, 230).orderByAsc(OrderEntity::getId).last("limit 0,10");
        List<OrderEntity> list = orderMapper.selectList(queryWrapper);
        List<Long> ids = list.stream().map(l -> l.getId()).collect(Collectors.toList());
        System.out.println(ids);
    }
}
