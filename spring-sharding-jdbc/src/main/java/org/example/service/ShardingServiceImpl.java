package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.dao.CourseMapper;
import org.example.dao.OrderMapper;
import org.example.entities.CourseEntity;
import org.example.entities.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShardingServiceImpl implements ShardingService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<CourseEntity> listCourse(Integer limit, Integer offset) {
        LambdaQueryWrapper<CourseEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(CourseEntity::getId).last(getLast(limit, offset));
        return courseMapper.selectList(queryWrapper);
    }

    @Override
    public List<OrderEntity> listOrder(Integer limit, Integer offset) {
        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(OrderEntity::getId).last(getLast(limit, offset));
        return orderMapper.selectList(queryWrapper);
    }

    private String getLast(Integer limit, Integer offset) {
        StringBuilder last = new StringBuilder("limit ");
        last.append(offset).append(",").append(limit);
        return last.toString();
    }
}
