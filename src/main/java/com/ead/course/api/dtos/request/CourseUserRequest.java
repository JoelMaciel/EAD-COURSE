package com.ead.course.api.dtos.request;

import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.CourseUser;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseUserRequest {

    private UUID courseId;
    @NotNull
    private UUID userId;

    public static CourseUser toEntity(Course course, UUID userId) {
        return CourseUser.builder()
                .course(course)
                .userId(userId)
                .build();
    }
}
