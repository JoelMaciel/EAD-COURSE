package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.LessonRequest;
import com.ead.course.api.dtos.response.LessonDTO;
import com.ead.course.domain.exceptions.LessonIntoModuleNotFoundException;
import com.ead.course.domain.models.Lesson;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.services.LessonService;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;

    @Override
    public List<LessonDTO> findAll(UUID moduleId) {
        List<Lesson> lessonList = lessonRepository.findByModule_ModuleId(moduleId);
        return lessonList.stream()
                .map(LessonDTO::toDTO)
                .collect(Collectors.toList());
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
    public void delele(UUID moduleId, UUID lessonId) {
        Lesson lesson = findLessonIntoModule(moduleId, lessonId);
        lessonRepository.delete(lesson);
    }

    @Override
    public Lesson findLessonIntoModule(UUID moduleId, UUID lessonId) {
        return lessonRepository.findByModuleModuleIdAndLessonId(moduleId, lessonId)
                .orElseThrow(() -> new LessonIntoModuleNotFoundException(moduleId, lessonId));

    }

}
