package com.ead.course.api.dtos.response;

import com.ead.course.domain.models.CourseUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseUserDTO {

    private UUID id;
    private UUID userId;
    private CourseDTO course;

    public static CourseUserDTO toDTO(CourseUser courseUser) {
        return CourseUserDTO.builder()
                .id(courseUser.getId())
                .userId(courseUser.getUser().getUserId())
                .course(CourseDTO.toDTO(courseUser.getCourse()))
                .build();
    }

}
