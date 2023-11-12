package com.ead.course.api.dtos.request;

import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.CourseUser;
import com.ead.course.domain.models.User;
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

    public static CourseUser toEntity(Course course, User user) {
        return CourseUser.builder()
                .course(course)
                .user(user)
                .build();
    }
}
