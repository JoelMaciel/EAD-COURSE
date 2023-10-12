package com.ead.course.domain.services;

import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.domain.models.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    CourseDTO findById(UUID courseId);

    Course searchById(UUID courseId);

    void delete(UUID courseId);

    CourseDTO save(CourseRequest courseRequest);

    CourseDTO update(UUID courseId, CourseRequest courseRequest);

    List<CourseDTO> findAll();
}
