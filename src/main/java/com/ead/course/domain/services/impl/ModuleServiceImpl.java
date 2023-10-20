package com.ead.course.domain.services.impl;

import com.ead.course.api.controllers.ModuleController;
import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.domain.exceptions.ModuleIntoCourseNotFoundException;
import com.ead.course.domain.exceptions.ModuleNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseService courseService;

    @Override
    public Page<ModuleDTO> findAllModules(Specification<Module> spec, Pageable pageable) {
        Page<Module> modulesPage = findAllModulesByCourse(spec, pageable);
        addHateoasLinks(modulesPage);
        return modulesPage.map(ModuleDTO::toDTO);
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

    @Override
    public Module searchByModule(UUID moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));
    }

    @Override
    public Page<Module> findAllModulesByCourse(Specification<Module> spec, Pageable pageable) {
        return moduleRepository.findAll(spec, pageable);
    }

    private void addHateoasLinks(Page<Module> modules) {
        if (!modules.isEmpty()) {
            for (Module module : modules) {
                module.add(linkTo(methodOn(ModuleController.class)
                        .getOneModule(module.getCourse().getCourseId(), module.getModuleId())).withSelfRel());
            }
        }
    }

}
