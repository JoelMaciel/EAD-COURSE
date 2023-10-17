package com.ead.course.utils;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.Module;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestHelper {
    public static ModuleRequest createModuleRequest(String title, String description) {
        return ModuleRequest.builder()
                .title(title)
                .description(description)
                .build();
    }

    public static Course createCourse(String name, String description, String imageUrl,
                               CourseStatus status, UUID instructor, CourseLevel level) {
        return Course.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .courseStatus(status)
                .userInstructor(instructor)
                .courseLevel(level)
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    public static Module createModule(ModuleRequest request, Course course) {
        return ModuleRequest.toEntity(request).toBuilder()
                .creationDate(LocalDateTime.now())
                .course(course)
                .build();
    }
}
