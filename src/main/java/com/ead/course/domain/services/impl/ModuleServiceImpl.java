package com.ead.course.domain.services.impl;

import com.ead.course.domain.models.Lesson;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(Module module) {

        List<Lesson> lessonList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
        if (!lessonList.isEmpty()) {
            lessonRepository.deleteAll(lessonList);
        }
        moduleRepository.delete(module);
    }
}
