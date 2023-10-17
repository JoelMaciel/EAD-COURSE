package com.ead.course.api.controllers;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses/{courseId}/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public List<ModuleDTO> getAllModules(@PathVariable UUID courseId) {
        return moduleService.findAllModulesByCourse(courseId);
    }

    @GetMapping("/{moduleId}")
    public ModuleDTO getOneCourse(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        return moduleService.findByModule(courseId, moduleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleDTO saveModule(@PathVariable UUID courseId, @RequestBody @Valid ModuleRequest request) {
        return moduleService.save(courseId, request);
    }
    @PutMapping("/{moduleId}")
    public ModuleDTO updateModule(@PathVariable UUID courseId, @PathVariable UUID moduleId,
                                   @RequestBody @Valid ModuleRequest request) {
        return moduleService.update(courseId, moduleId,request);
    }
    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        moduleService.delete(courseId, moduleId);
    }
}
