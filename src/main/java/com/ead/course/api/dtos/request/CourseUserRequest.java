package com.ead.course.api.dtos.request;

import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.CourseUser;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CourseUserRequest {

    private UUID userId;

    public static CourseUser toEntity(Course course, UUID userId) {
        return CourseUser.builder()
                .course(course)
                .userId(userId)
                .build();
    }
}
