package com.ead.course.api.dtos.request;

import com.ead.course.domain.models.Lesson;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonRequest {

    @NotBlank
    private String title;
    private String description;

    @NotBlank
    private String videoUrl;

    public static Lesson toEntity(LessonRequest request) {
        return Lesson.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creationDate(LocalDateTime.now())
                .videoUrl(request.getVideoUrl())
                .build();
    }
}