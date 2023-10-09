package com.ead.course.domain.services.impl;

import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.Lesson;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private ModuleRepository moduleRepository;
    private LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(Course course) {
        List<Module> moduleList = moduleRepository.findAllModulesIntoCourse(course.getCourseId());
        if (!moduleList.isEmpty()) {
            for (Module module : moduleList) {
                List<Lesson> lessonList = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessonList.isEmpty()) {
                    lessonRepository.deleteAll(lessonList);
                }
            }
            moduleRepository.deleteAll(moduleList);
        }
        courseRepository.delete(course);
    }
}
