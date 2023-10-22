package com.ead.course.domain.services;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.domain.models.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface ModuleService {

    ModuleDTO findByModule(UUID courseId, UUID moduleId);

    ModuleDTO save(UUID courseId, ModuleRequest request);

    ModuleDTO update(UUID courseId, UUID moduleId, ModuleRequest request);

    void delete(UUID courseId, UUID moduleId);

    Module findModuleIntoCourse(UUID courseId, UUID moduleId);

    Module searchByModule(UUID moduleId);

    Page<Module> findAllModulesByCourse(Specification<Module> spec, Pageable pageable);

    Page<ModuleDTO> findAllModules(Specification<Module> and, Pageable pageable);
}
