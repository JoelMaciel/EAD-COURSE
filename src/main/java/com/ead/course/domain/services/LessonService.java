package com.ead.course.domain.services;

import com.ead.course.api.dtos.request.LessonRequest;
import com.ead.course.api.dtos.response.LessonDTO;
import com.ead.course.domain.models.Lesson;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    List<LessonDTO> findAll(UUID moduleId);

    LessonDTO findById(UUID moduleId, UUID lessonId);

    LessonDTO save(UUID moduleId, LessonRequest lessonRequest);

    LessonDTO update(UUID moduleId, UUID lessonId, LessonRequest lessonRequest);

    void delele(UUID moduleId, UUID lessonId);

    Lesson findLessonIntoModule(UUID moduleId, UUID lessonId);

}
