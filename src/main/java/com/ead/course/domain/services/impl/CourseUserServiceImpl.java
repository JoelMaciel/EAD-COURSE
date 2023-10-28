package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.domain.exceptions.ExistsCourseAndUserException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.CourseUser;
import com.ead.course.domain.repositories.CourseUserRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.CourseUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final CourseService courseService;

    @Override
    public CourseUserDTO save(UUID courseId, CourseUserRequest courseUserRequest) {
        Course course = courseService.searchById(courseId);
       if( existsByCourseAndUserId(course, courseUserRequest.getUserId())) {
           throw new ExistsCourseAndUserException(courseId);
       }
        CourseUser courseUser = courseUserRepository
                .save(CourseUserRequest.toEntity(course, courseUserRequest.getUserId()));

       return CourseUserDTO.toDTO(courseUser);
    }

    @Override
    public boolean existsByCourseAndUserId(Course course, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(course, userId);
    }
}
