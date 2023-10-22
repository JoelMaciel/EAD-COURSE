package com.ead.course.domain.services.impl;

import com.ead.course.api.controllers.LessonController;
import com.ead.course.api.dtos.request.LessonRequest;
import com.ead.course.api.dtos.response.LessonDTO;
import com.ead.course.domain.exceptions.LessonIntoModuleNotFoundException;
import com.ead.course.domain.models.Lesson;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.services.LessonService;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;

    public Page<LessonDTO> findAll(Specification<Lesson> spec, Pageable pageable) {
        Page<Lesson> lessonPages = findAllLessonSpec(spec, pageable);
        addHateoasLinks(lessonPages);
        return lessonPages.map(LessonDTO::toDTO);
    }

    @Override
    public LessonDTO findById(UUID moduleId, UUID lessonId) {
        return LessonDTO.toDTO(findLessonIntoModule(moduleId, lessonId));
    }

    @Override
    @Transactional
    public LessonDTO save(UUID moduleId, LessonRequest lessonRequest) {
        Module module = moduleService.searchByModule(moduleId);
        Lesson lesson = LessonRequest.toEntity(lessonRequest).toBuilder()
                .module(module)
                .build();
        return LessonDTO.toDTO(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public LessonDTO update(UUID moduleId, UUID lessonId, LessonRequest lessonRequest) {
        Lesson lesson = findLessonIntoModule(moduleId, lessonId).toBuilder()
                .title(lessonRequest.getTitle())
                .description(lessonRequest.getDescription())
                .videoUrl(lessonRequest.getVideoUrl())
                .build();

        return LessonDTO.toDTO(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public void delete(UUID moduleId, UUID lessonId) {
        Lesson lesson = findLessonIntoModule(moduleId, lessonId);
        lessonRepository.delete(lesson);
    }

    @Override
    public Lesson findLessonIntoModule(UUID moduleId, UUID lessonId) {
        return lessonRepository.findByModuleModuleIdAndLessonId(moduleId, lessonId)
                .orElseThrow(() -> new LessonIntoModuleNotFoundException(moduleId, lessonId));

    }

    @Override
    public Page<Lesson> findAllLessonSpec(Specification<Lesson> spec, Pageable pageable) {
        return lessonRepository.findAll(spec, pageable);
    }

    private void addHateoasLinks(Page<Lesson> lessons) {
        if (!lessons.isEmpty()) {
            for (Lesson lesson : lessons) {
                lesson.add(linkTo(methodOn(LessonController.class)
                        .getOneLesson(lesson.getModule().getModuleId(), lesson.getLessonId())).withSelfRel());
            }
        }
    }

}
