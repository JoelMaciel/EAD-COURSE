package com.ead.course.domain.services.impl;

import com.ead.course.api.clients.AuthUserClient;
import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.api.dtos.response.UserDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.containers.DatabaseContainerConfiguration;
import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.repositories.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Import(DatabaseContainerConfiguration.class)
class CourseServiceImplIT {

    public static final String MSG_INVALID_COURSEID = "There is no course registered with UUID %s";
    private static final String BASE_TEST_COURSE_NAME = "Test Course ";
    private static final int PAGE_SIZE = 10;
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthUserClient authUserClient;
    private CourseRequest courseRequest;
    private Course course;
    private Specification<Course> spec;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        String uniqueCourseName = BASE_TEST_COURSE_NAME + "SomeName " + System.currentTimeMillis();

        spec = createCourseSpecification();


        spec = createCourseSpecification();
        pageable = PageRequest.of(0, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "courseId"));

        courseService = new CourseServiceImpl(courseRepository, authUserClient);

        courseRequest = CourseRequest.builder()
                .name(uniqueCourseName)
                .description("Test Description One")
                .imageUrl("http://testimage1.com")
                .courseStatus(CourseStatus.INPROGRESS)
                .userInstructor(UUID.randomUUID())
                .courseLevel(CourseLevel.BEGINNER)
                .build();

        course = CourseRequest.toEntity(courseRequest).toBuilder()
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteAll();
    }

    @Test
    @DisplayName("Given Courses Exist When Calling FindAll Then Return Page of CoursesDTOs Successfully")
    void givenCoursesExist_WhenCallingFindAll_ThenReturnPageOfCourses() {
        UUID userId = UUID.fromString("99735306-994d-46f9-82a7-4116145a5678");
        Course savedCourse = courseRepository.save(course);

        assertNotNull(savedCourse);
        assertNotNull(savedCourse.getCourseId());

        long count = courseRepository.count();
        assertEquals(1, count);

        Page<CourseDTO> courseDTOS = courseService.findAll(spec, pageable, userId);

        assertNotNull(courseDTOS);
        assertEquals(0, courseDTOS.getTotalElements(), "Expected two courses to be returned from the service");
    }

    @Test
    @DisplayName("Given Valid CourseId When Calling FindById Then It Should Return Course Successfully")
    void givenValidCourseId_WhenCallingFindById_ThenReturnCourse() {
        courseRepository.save(course);
        CourseDTO courseDTO = courseService.findById(course.getCourseId());

        assertNotNull(courseDTO);
        assertEquals(courseDTO.getName(), course.getName());
    }

    @Test
    @DisplayName("Given Invalid CourseId When Calling FindById Then It Should Throw CourseNotFoundException")
    void givenInvalidCourseId_WhenCallingFindById_ThenThrowException() {
        UUID courseId = UUID.randomUUID();
        assertThrows(CourseNotFoundException.class, () -> courseService.findById(courseId));
    }

    @Test
    @DisplayName("Given Valid CourseRequest When Calling Save Then It Should Save and Return CourseDTO Successfully")
    void givenValidCourseRequest_WhenCallingSave_ThenReturnSavedCourseDTO() {
        UUID userIdInstructor = UUID.fromString("99735306-994d-46f9-82a7-4116145a5678");
        UserDTO userInstructor = authUserClient.getOneUserById(userIdInstructor);
        CourseRequest request = CourseRequest.builder()
                .name("Integration Test Course")
                .description("Integration Test Description")
                .imageUrl("http://integrationtestimage.com")
                .courseStatus(CourseStatus.INPROGRESS)
                .userInstructor(userIdInstructor)
                .courseLevel(CourseLevel.BEGINNER)
                .build();

        CourseDTO courseDTO = courseService.save(request);

        assertNotNull(courseDTO);
        assertNotNull(courseDTO.getCourseId());
        assertEquals(request.getName(), courseDTO.getName());
        assertEquals(request.getDescription(), courseDTO.getDescription());

        Optional<Course> courseInDb = courseRepository.findById(courseDTO.getCourseId());
        assertTrue(courseInDb.isPresent());
        assertEquals(request.getName(), courseInDb.get().getName());

    }

    @Test
    @DisplayName("Given Valid CourseId and CourseRequest When Calling Update Then It Should Update and Return Updated CourseDTO Successfully")
    void givenValidCourseIdAndRequest_WhenCallingUpdate_ThenReturnUpdatedCourseDTO() {
        Course savedCourse = courseRepository.save(course);

        CourseRequest updateRequest = CourseRequest.builder()
                .name("Updated Test Course")
                .description("Updated Test Description")
                .imageUrl("http://updatedtestimage.com")
                .courseStatus(CourseStatus.INPROGRESS)
                .userInstructor(UUID.randomUUID())
                .courseLevel(CourseLevel.ADVANCED)
                .build();

        CourseDTO updateCourseDTO = courseService.update(savedCourse.getCourseId(), updateRequest);

        assertNotNull(updateCourseDTO);
        assertEquals(updateRequest.getName(), updateCourseDTO.getName());
        assertEquals(updateRequest.getDescription(), updateCourseDTO.getDescription());

        Optional<Course> courseInDb = courseRepository.findById(savedCourse.getCourseId());
        assertTrue(courseInDb.isPresent());
        assertEquals(updateRequest.getName(), courseInDb.get().getName());
        assertEquals(updateRequest.getDescription(), courseInDb.get().getDescription());
    }

    @Test
    @DisplayName("Given Invalid CourseId When Calling Update Then It Should Throw CourseNotFoundException")
    void givenInvalidCourseId_WhenCallingUpdate_ThenThrowCourseNotFoundException() {
        UUID invalidCourseId = UUID.randomUUID();

        CourseRequest updateRequest = CourseRequest.builder()
                .name("Updated Test Course")
                .description("Updated Test Description")
                .imageUrl("http://updatedtestimage.com")
                .courseStatus(CourseStatus.INPROGRESS)
                .userInstructor(UUID.randomUUID())
                .courseLevel(CourseLevel.ADVANCED)
                .build();

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class,
                () -> courseService.update(invalidCourseId, updateRequest));

        String expectedMessage = String.format(MSG_INVALID_COURSEID, invalidCourseId);
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Given Valid CourseId When Calling Delete Then Course Should Be Deleted Successfully")
    void givenValidCourseId_WhenCallingDelete_ThenCourseShouldBeDeleted() {
        Course savedCourse = courseRepository.save(course);

        Optional<Course> courseOptional = courseRepository.findById(savedCourse.getCourseId());
        assertTrue(courseOptional.isPresent());

        courseService.delete(savedCourse.getCourseId());

        Optional<Course> optionalDeletedCourse = courseRepository.findById(savedCourse.getCourseId());
        assertFalse(optionalDeletedCourse.isPresent());
    }


    @Test
    @DisplayName("Given Invalid CourseId When Calling Delete Then It Should Throw CourseNotFoundException")
    void givenInvalidCourseId_WhenCallingDelete_ThenThrowCourseNotFoundException() {
        UUID invalidCourseId = UUID.randomUUID();

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class,
                () -> courseService.delete(invalidCourseId));

        String expectedMessage = String.format(MSG_INVALID_COURSEID, invalidCourseId);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    private Specification<Course> createCourseSpecification() {
        return new SpecificationTemplate.CourseSpec() {
            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("courseLevel"), CourseLevel.BEGINNER));
                predicates.add(criteriaBuilder.equal(root.get("courseStatus"), CourseStatus.INPROGRESS));
                predicates.add(criteriaBuilder.like(root.get("name"), "%SomeName%"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

}
