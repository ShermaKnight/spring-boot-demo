package org.example.service;

import org.example.entities.CourseEntity;
import org.example.entities.OrderEntity;

import java.util.List;

public interface ShardingService {

    List<CourseEntity> listCourse(Integer limit, Integer offset);

    List<OrderEntity> listOrder(Integer limit, Integer offset);
}
