package com.ead.course.domain.services;

import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.domain.models.Course;

import java.util.UUID;

public interface CourseUserService {


    CourseUserDTO save(UUID courseId, CourseUserRequest courseUserRequest);

    boolean existsByCourseAndUserId(Course course, UUID userId);
}
