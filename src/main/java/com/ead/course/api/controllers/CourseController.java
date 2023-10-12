package com.ead.course.api.controllers;

import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.domain.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.findAll();
    }

    @GetMapping("/{courseId}")
    public CourseDTO getOneCourse(@PathVariable UUID courseId) {
        return courseService.findById(courseId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO saveCourse(@RequestBody @Valid CourseRequest courseRequest) {
        return courseService.save(courseRequest);
    }

    @PutMapping("/{courseId}")
    public CourseDTO updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseRequest courseRequest) {
        return courseService.update(courseId,courseRequest);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable UUID courseId) {
        courseService.delete(courseId);
    }
}
