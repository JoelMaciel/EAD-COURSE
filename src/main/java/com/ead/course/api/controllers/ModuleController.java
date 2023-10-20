package com.ead.course.api.controllers;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.domain.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses/{courseId}/modules")
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public Page<ModuleDTO> getAllModules(@PathVariable UUID courseId, SpecificationTemplate.ModuleSpec spec,
                                         @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable) {
        return moduleService.findAllModules(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable);
    }

    @GetMapping("/{moduleId}")
    public ModuleDTO getOneModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
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
        return moduleService.update(courseId, moduleId, request);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        moduleService.delete(courseId, moduleId);
    }
}
