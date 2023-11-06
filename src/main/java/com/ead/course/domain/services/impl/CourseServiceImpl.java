package com.ead.course.domain.services.impl;

import com.ead.course.api.clients.AuthUserClient;
import com.ead.course.api.controllers.CourseController;
import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.api.dtos.response.UserDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.domain.enums.UserType;
import com.ead.course.domain.exceptions.BusinessException;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.exceptions.UserNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.CourseUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final AuthUserClient authUserClient;
    private final CourseUserService courseUserService;

    public CourseServiceImpl(CourseRepository courseRepository,
                             AuthUserClient authUserClient,
                             @Lazy CourseUserService courseUserService) {
        this.courseRepository = courseRepository;
        this.authUserClient = authUserClient;
        this.courseUserService = courseUserService;
    }

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
        validateInstructorOrAdmin(courseRequest.getUserInstructor());

        Course course = buildCourseFromRequest(courseRequest);

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
        if (courseUserService.existsByCourseId(courseId)) {
            authUserClient.deleteCourseInAuthUser(course.getCourseId());
        }
        courseRepository.delete(course);

    }

    @Override
    public Course searchById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    private void validateInstructorOrAdmin(UUID userInstructor) {
        try {
            UserDTO user = authUserClient.getOneUserById(userInstructor);
            if (user.getUserType().equals(UserType.STUDENT)) {
                throw new BusinessException("User must be INSTRUCTOR or ADMIN");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new UserNotFoundException(userInstructor);
            }
        }
    }

    private Course buildCourseFromRequest(CourseRequest courseRequest) {
        return CourseRequest.toEntity(courseRequest).toBuilder()
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
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
