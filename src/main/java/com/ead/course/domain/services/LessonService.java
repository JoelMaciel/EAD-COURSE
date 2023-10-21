package com.ead.course.domain.services;

import com.ead.course.api.dtos.request.LessonRequest;
import com.ead.course.api.dtos.response.LessonDTO;
import com.ead.course.domain.models.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface LessonService {

    Page<LessonDTO> findAll(Specification<Lesson> and, Pageable pageable);

    LessonDTO findById(UUID moduleId, UUID lessonId);

    LessonDTO save(UUID moduleId, LessonRequest lessonRequest);

    LessonDTO update(UUID moduleId, UUID lessonId, LessonRequest lessonRequest);

    void delete(UUID moduleId, UUID lessonId);

    Lesson findLessonIntoModule(UUID moduleId, UUID lessonId);

    Page<Lesson> findAllLessonSpec(Specification<Lesson> and, Pageable pageable);
}
