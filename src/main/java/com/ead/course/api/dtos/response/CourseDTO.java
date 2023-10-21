package com.ead.course.api.dtos.response;

import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.ead.course.domain.models.Course;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO extends RepresentationModel<CourseDTO> {

    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private CourseLevel courseLevel;
    private UUID userInstructor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime updateDate;

    public static CourseDTO toDTO(Course course) {
        CourseDTO courseDTO = CourseDTO.builder()
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

        courseDTO.add(course.getLinks());
        return courseDTO;
    }
}
