package com.ead.course.api.dtos.response;

import com.ead.course.domain.models.Lesson;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDTO {

    private UUID lessonId;
    private String title;
    private String description;
    private String videoUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    public static LessonDTO toDTO(Lesson lesson) {
        return LessonDTO.builder()
                .lessonId(lesson.getLessonId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .videoUrl(lesson.getVideoUrl())
                .creationDate(lesson.getCreationDate())
                .build();
    }
}
