package com.ead.course.domain.services.impl;

import com.ead.course.api.controllers.CourseController;
import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.request.CourseUserRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.api.dtos.response.CourseUserDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.domain.enums.UserStatus;
import com.ead.course.domain.enums.UserType;
import com.ead.course.domain.exceptions.BusinessException;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.exceptions.ExistsCourseAndUserException;
import com.ead.course.domain.exceptions.UserBlockedException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.CourseUser;
import com.ead.course.domain.models.User;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.repositories.CourseUserRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.UserService;
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
public class CourseServiceImpl implements CourseService {

    public static final String MSG_USER_ADMIN_OR_INSTRUCTOR = "User must be INSTRUCTOR or ADMIN";
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CourseUserRepository courseUserRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService, CourseUserRepository courseUserRepository) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    public Page<CourseDTO> findAll(Specification<Course> spec, Pageable pageable, UUID userId) {
        Specification<Course> effectiveSpec = (userId != null) ?
                SpecificationTemplate.courseUserId(userId).and(spec) : spec;
        Page<Course> coursesPage = courseRepository.findAll(effectiveSpec, pageable);

        addHateoasLinks(coursesPage);
        return coursesPage.map(CourseDTO::toDTO);
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
    public CourseUserDTO saveSubscriptionUserInCourse(UUID courseId, CourseUserRequest courseUserRequest) {
        Course course = searchById(courseId);
        validateCourseAndUser(courseId, courseUserRequest.getUserId());

        User user = userService.searchById(courseUserRequest.getUserId());
        validateUserStatus(user);

        CourseUser courseUser = CourseUserRequest.toEntity(course, user);
        return CourseUserDTO.toDTO(courseUserRepository.save(courseUser));
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courseRepository.existsByCourseAndUser(courseId, userId).intValue() > 0;
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

    private void validateInstructorOrAdmin(UUID userInstructor) {
        User user = userService.searchById(userInstructor);

        if (user.getUserType().equals(UserType.STUDENT.toString())) {
            throw new BusinessException(MSG_USER_ADMIN_OR_INSTRUCTOR);
        }
    }

    private Course buildCourseFromRequest(CourseRequest courseRequest) {
        return CourseRequest.toEntity(courseRequest).toBuilder()
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    private void validateCourseAndUser(UUID courseId, UUID userId) {
        if (existsByCourseAndUser(courseId, userId)) {
            throw new ExistsCourseAndUserException(courseId);
        }
    }

    private void validateUserStatus(User user) {
        if (user.getUserStatus().equals(UserStatus.BLOCKED.toString())) {
            throw new UserBlockedException();
        }
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
