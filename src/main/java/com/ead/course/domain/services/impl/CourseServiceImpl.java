package com.ead.course.domain.services.impl;

import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
}
