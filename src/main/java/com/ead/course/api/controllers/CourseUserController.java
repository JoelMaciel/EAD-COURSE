package com.ead.course.api.controllers;

import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.domain.models.User;
import com.ead.course.domain.services.UserService;
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

    private final UserService userService;

    @GetMapping("/api/courses/{courseId}/users")
    public Page<User> getAllUsersByCourse(SpecificationTemplate.UserSpec spec, @PageableDefault(page = 0,
        size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable UUID courseId) {
        return userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable);
    }

    @PostMapping("/api/courses/{courseId}/users/subscription")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseUserDTO saveSubscriptionUserInCourse(@PathVariable UUID courseId, @RequestBody
    @Valid CourseUserRequest courseUserRequest) {
        //return userService.saveSubscriptionUserInCourse(courseId, courseUserRequest);
        return null;
    }

}
