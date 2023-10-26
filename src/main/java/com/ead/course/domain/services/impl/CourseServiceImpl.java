package com.ead.course.domain.services.impl;

import com.ead.course.api.controllers.CourseController;
import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.services.CourseService;
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
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Page<CourseDTO> findAll(Specification<Course> spec, Pageable pageable, UUID userId) {
        Page<Course> coursesPage = null;
        if (userId != null) {
            coursesPage = courseRepository.findAll(
                    SpecificationTemplate.courseUserId(userId).and(spec), pageable);
        } else {
            coursesPage = findAll(spec, pageable);
        }

        addHateoasLinks(coursesPage);
        return coursesPage.map(CourseDTO::toDTO);
    }

    private Page<Course> findAll(Specification<Course> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
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

    private void addHateoasLinks(Page<Course> courses) {
        if (!courses.isEmpty()) {
            for (Course course : courses) {
                course.add(linkTo(methodOn(CourseController.class)
                        .getOneCourse(course.getCourseId())).withSelfRel());
            }
        }
    }
}
