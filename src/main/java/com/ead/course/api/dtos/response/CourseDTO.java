package com.ead.course.api.dtos.response;

import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.ead.course.domain.models.Course;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {

    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private CourseLevel courseLevel;
    private UUID userInstructor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    public static CourseDTO toDTO(Course course) {
        return CourseDTO.builder()
                .courseId(course.getCourseId())
                .name(course.getName())
                .description(course.getDescription())
                .imageUrl(course.getImageUrl())
                .courseStatus(course.getCourseStatus())
                .courseLevel(course.getCourseLevel())
                .userInstructor(course.getUserInstructor())
                .creationDate(course.getCreationDate())
                .updateDate(course.getUpdateDate())
                .build();
    }
}
