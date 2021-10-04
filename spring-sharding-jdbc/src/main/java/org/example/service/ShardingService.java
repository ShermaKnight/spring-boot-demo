package org.example.service;

import org.example.entities.Course;

import java.util.List;

public interface ShardingService {

    List<Course> list(Integer limit, Integer offset);
}
