package com.ead.course.api.controllers;

import com.ead.course.api.clients.AuthUserClient;
import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.api.dtos.response.UserDTO;
import com.ead.course.domain.services.CourseUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CourseUserController {

    private final AuthUserClient authUserClient;
    private final CourseUserService courseUserService;

    @GetMapping("/api/courses/{courseId}/users")
    public Page<UserDTO> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId",
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID courseId) {
        courseUserService.searchByCourseId(courseId);
        return authUserClient.getAllUsersByCourse(courseId, pageable);
    }

    @PostMapping("/api/courses/{courseId}/users/subscription")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseUserDTO saveSubscriptionUserInCourse(@PathVariable UUID courseId, @RequestBody
    @Valid CourseUserRequest courseUserRequest) {
        return courseUserService.saveSubscriptionUserInCourse(courseId, courseUserRequest);
    }

    @DeleteMapping("/api/courses/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourseUserByUser(@PathVariable UUID userId) {
        courseUserService.deleteCourseUserByUser(userId);
    }
}
