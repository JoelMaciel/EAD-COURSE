package com.ead.course.domain.services;

import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.domain.models.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface CourseService {

    Page<CourseDTO> findAll(Specification<Course> spec, Pageable pageable, UUID userId);

    CourseDTO findById(UUID courseId);

    Course searchById(UUID courseId);

    void delete(UUID courseId);

    CourseDTO save(CourseRequest courseRequest);

    CourseDTO update(UUID courseId, CourseRequest courseRequest);

    CourseUserDTO saveSubscriptionUserInCourse(UUID courseId, CourseUserRequest courseUserRequest);

    boolean existsByCourseAndUser(UUID courseId, UUID userId);
}
