package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
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
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
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
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private CourseRepository courseRepository;
    private CourseRequest courseRequest;
    private Course course;

    @BeforeEach
    void setUp() {
        String uniqueCourseName = "Test Course One " + System.currentTimeMillis();

        courseService = new CourseServiceImpl(courseRepository);

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
    @DisplayName("Given Courses Exist When Calling FindAll Then Return List of CoursesDTOs Successfully")
    void givenCoursesExist_WhenCallingFindAll_ThenReturnListOfCourses() {
        courseRepository.save(course);

        String uniqueCourseName2 = "Test Course Two " + System.currentTimeMillis();
        CourseRequest courseRequest2 = CourseRequest.builder()
                .name(uniqueCourseName2)
                .description("Test Description Two")
                .imageUrl("http://testimage2.com")
                .courseStatus(CourseStatus.INPROGRESS)
                .userInstructor(UUID.randomUUID())
                .courseLevel(CourseLevel.BEGINNER)
                .build();
        Course course2 = CourseRequest.toEntity(courseRequest2).toBuilder()
                .creationDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        courseRepository.save(course2);

        List<CourseDTO> courseDTOS = courseService.findAll();

        assertNotNull(courseDTOS);
        assertEquals(2, courseDTOS.size());
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
        CourseRequest request = CourseRequest.builder()
                .name("Integration Test Course")
                .description("Integration Test Description")
                .imageUrl("http://integrationtestimage.com")
                .courseStatus(CourseStatus.INPROGRESS)
                .userInstructor(UUID.randomUUID())
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

}
