package com.ead.course.api.controllers;

import com.ead.course.api.dtos.request.LessonRequest;
import com.ead.course.api.dtos.response.LessonDTO;
import com.ead.course.domain.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/modules/{moduleId}/lessons")
public class LessonController {

    private final LessonService lessonService;


    @GetMapping
    public List<LessonDTO> getAllLessons(@PathVariable UUID moduleId) {
        return lessonService.findAll(moduleId);
    }

    @GetMapping("/{lessonId}")
    public LessonDTO getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        return lessonService.findById(moduleId, lessonId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDTO saveLesson(@PathVariable UUID moduleId, @RequestBody @Valid LessonRequest lessonRequest) {
        return lessonService.save(moduleId, lessonRequest);
    }

    @PutMapping("/{lessonId}")
    public LessonDTO updateLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId,
                                  @RequestBody @Valid LessonRequest lessonRequest) {
        return lessonService.update(moduleId, lessonId, lessonRequest);
    }

    @DeleteMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        lessonService.delele(moduleId, lessonId);
    }

}
