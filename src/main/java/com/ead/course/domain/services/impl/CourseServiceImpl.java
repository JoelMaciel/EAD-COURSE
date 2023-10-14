package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<CourseDTO> findAll() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO findById(UUID courseId) {
        return CourseDTO.toDTO(searchById(courseId));
    }

    @Transactional
    @Override
    public CourseDTO save(CourseRequest courseRequest) {
        Course course = CourseRequest.toEntity(courseRequest).toBuilder()
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        return CourseDTO.toDTO(courseRepository.save(course));
    }

    @Transactional
    @Override
    public CourseDTO update(UUID courseId, CourseRequest courseRequest) {
        Course course = searchById(courseId).toBuilder()
                .name(courseRequest.getName())
                .description(courseRequest.getDescription())
                .imageUrl(courseRequest.getImageUrl())
                .courseStatus(courseRequest.getCourseStatus())
                .courseLevel(courseRequest.getCourseLevel())
                .build();

        return CourseDTO.toDTO(courseRepository.save(course));
    }

    @Transactional
    @Override
    public void delete(UUID courseId) {
        Course course = searchById(courseId);
        courseRepository.delete(course);
    }

    @Override
    public Course searchById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }
}
