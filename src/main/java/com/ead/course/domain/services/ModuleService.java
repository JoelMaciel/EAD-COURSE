package com.ead.course.domain.services;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.domain.models.Module;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    ModuleDTO findByModule(UUID courseId, UUID moduleId);

    List<Module> findAllByCourse(UUID courseId);

    ModuleDTO save(UUID courseId, ModuleRequest request);

    ModuleDTO update(UUID courseId, UUID moduleId, ModuleRequest request);

    void delete(UUID courseId, UUID moduleId);

    Module findModuleIntoCourse(UUID courseId, UUID moduleId);

}
