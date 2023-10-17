package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.domain.exceptions.ModuleIntoCourseNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
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
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseService courseService;

    @Override
    public List<ModuleDTO> findAllModulesByCourse(UUID courseId) {
        List<Module> moduleList = moduleRepository.findByCourseCourseId(courseId);
        return moduleList.stream()
                .map(ModuleDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleDTO findByModule(UUID courseId, UUID moduleId) {
        Module module = findModuleIntoCourse(courseId, moduleId);
        return ModuleDTO.toDTO(module);
    }

    @Override
    public ModuleDTO save(UUID courseId, ModuleRequest request) {
        Course course = courseService.searchById(courseId);
        Module module = ModuleRequest.toEntity(request).toBuilder()
                .creationDate(LocalDateTime.now())
                .course(course)
                .build();

        return ModuleDTO.toDTO(moduleRepository.save(module));
    }

    @Override
    @Transactional
    public ModuleDTO update(UUID courseId, UUID moduleId, ModuleRequest request) {
        Module module = findModuleIntoCourse(courseId, moduleId).toBuilder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
        return ModuleDTO.toDTO(moduleRepository.save(module));
    }

    @Override
    @Transactional
    public void delete(UUID courseId, UUID moduleId) {
        Module module = findModuleIntoCourse(courseId, moduleId);
        moduleRepository.delete(module);
    }

    @Override
    public Module findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRepository.findByCourseCourseIdAndModuleId(courseId, moduleId)
                .orElseThrow(() -> new ModuleIntoCourseNotFoundException(courseId, moduleId));
    }
}
