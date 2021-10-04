package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.dao.CourseMapper;
import org.example.entities.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingServiceImpl implements ShardingService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> list(Integer limit, Integer offset) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Course::getId).last(getLast(limit, offset));
        return courseMapper.selectList(queryWrapper);
    }

    private String getLast(Integer limit, Integer offset) {
        StringBuilder last = new StringBuilder("limit ");
        last.append(offset).append(",").append(limit);
        return last.toString();
    }
}
