package com.ead.course.api.dtos.request;

import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.ead.course.domain.models.Course;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;
    private String imageUrl;

    @NotNull
    private CourseStatus courseStatus;

    @NotNull
    private UUID userInstructor;

    @NotNull
    private CourseLevel courseLevel;

    public static Course toEntity(CourseRequest request) {
        return Course.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .courseStatus(request.courseStatus)
                .userInstructor(request.userInstructor)
                .courseLevel(request.getCourseLevel())
                .build();
    }

}
