package org.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.dao.CourseMapper;
import org.example.entities.CourseEntity;
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
public class ShardingCourseTest {

    @Autowired
    private CourseMapper courseMapper;

    @Test
    public void insertBatch() {
        IntStream.range(0, 50).forEach(i -> {
            CourseEntity courseEntity = new CourseEntity();
            courseEntity.setCourseName("Nothing");
            courseEntity.setPrice(new BigDecimal(198.00 + i));
            courseEntity.setCreateTime(new Date());
            courseEntity.setUpdateTime(new Date());
            courseMapper.insert(courseEntity);
        });
    }

    @Test
    public void selectByCondition() {
        LambdaQueryWrapper<CourseEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(CourseEntity::getPrice, 230);
        Integer count = courseMapper.selectCount(queryWrapper);
        Assert.assertEquals(new Integer(18), count);
    }

    @Test
    public void selectByOrder() {
        LambdaQueryWrapper<CourseEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(CourseEntity::getPrice, 230).orderByAsc(CourseEntity::getId).last("limit 0,10");
        List<CourseEntity> list = courseMapper.selectList(queryWrapper);
        List<Long> ids = list.stream().map(l -> l.getId()).collect(Collectors.toList());
        System.out.println(ids);
    }
}
