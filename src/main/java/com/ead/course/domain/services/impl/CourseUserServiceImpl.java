package com.ead.course.domain.services.impl;

import com.ead.course.api.clients.AuthUserClient;
import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.api.dtos.response.UserDTO;
import com.ead.course.domain.enums.UserStatus;
import com.ead.course.domain.exceptions.ExistsCourseAndUserException;
import com.ead.course.domain.exceptions.UserBlockedException;
import com.ead.course.domain.exceptions.UserNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.CourseUser;
import com.ead.course.domain.repositories.CourseUserRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.CourseUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final CourseService courseService;
    private final AuthUserClient authUserClient;

    @Transactional
    @Override
    public CourseUserDTO saveSubscriptionUserInCourse(UUID courseId, CourseUserRequest courseUserRequest) {
        UserDTO userDTO;
        Course course = courseService.searchById(courseId);
        if (existsByCourseAndUserId(course, courseUserRequest.getUserId())) {
            throw new ExistsCourseAndUserException(courseId);
        }

        try {
            userDTO = authUserClient.getOneUserById(courseUserRequest.getUserId());
            if (userDTO.getUserStatus().equals(UserStatus.BLOCKED)) {
                throw new UserBlockedException();
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(courseUserRequest.getUserId());
            }
        }

        CourseUser courseUser = courseUserRepository
                .save(CourseUserRequest.toEntity(course, courseUserRequest.getUserId()));
        authUserClient.postSubscriptionUserInCourse(course, courseUserRequest.getUserId());

        return CourseUserDTO.toDTO(courseUser);
    }

    @Override
    public boolean existsByCourseAndUserId(Course course, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(course, userId);
    }

    @Override
    public boolean existsByCourseId(UUID courseId) {
        return courseUserRepository.existsByCourse_CourseId(courseId);
    }

    @Override
    public void searchByCourseId(UUID courseId) {
        courseService.searchById(courseId);
    }

    @Override
    @Transactional
    public void deleteCourseUserByUser(UUID userId) {
        if (courseUserRepository.existsByUserId(userId)) {
            courseUserRepository.deleteAllByUserId(userId);
        }
    }
}
