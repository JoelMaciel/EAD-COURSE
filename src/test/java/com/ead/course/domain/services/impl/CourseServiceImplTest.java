package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.CourseRequest;
import com.ead.course.api.dtos.response.CourseDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private UUID courseId;
    private Course courseOne;
    private Course courseTwo;
    private CourseRequest courseRequestOne;
    private Specification<Course> spec;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        courseRequestOne = buildCourseRequest("Teste Course", "Test Description",
                "http://testimage.com");

        CourseRequest courseRequestTwo = buildCourseRequest("Teste Course 2",
                "Test Description 2", "http://testimage2.com");

        courseOne = initCourseDates(CourseRequest.toEntity(courseRequestOne), 5, 1);
        courseTwo = initCourseDates(CourseRequest.toEntity(courseRequestTwo), 4, 2);

        spec = createCourseSpecification();
    }

    @DisplayName("Given a List of Courses When Calling FindAll Then It Should Page Courses Successfully")
    @Test
    void givenListOfCourse_WhenCallingFindAll_ThenShouldReturnPageCoursesSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "courseId"));

        Page<Course> coursePage = new PageImpl<>(Arrays.asList(courseOne, courseTwo));
        when(courseRepository.findAll(spec, pageable)).thenReturn(coursePage);

        Page<CourseDTO> courseDTOS = courseService.findAll(spec, pageable);

        assertNotNull(courseDTOS);
        assertEquals(2, courseDTOS.getTotalElements());
        assertEquals(courseOne.getName(), courseDTOS.getContent().get(0).getName());
        assertEquals(courseOne.getDescription(), courseDTOS.getContent().get(0).getDescription());
        assertEquals(courseOne.getCreationDate(), courseDTOS.getContent().get(0).getCreationDate());
        assertEquals(courseOne.getUpdateDate(), courseDTOS.getContent().get(0).getUpdateDate());

        assertEquals(courseTwo.getName(), courseDTOS.getContent().get(1).getName());
        assertEquals(courseTwo.getDescription(), courseDTOS.getContent().get(1).getDescription());
        assertEquals(courseTwo.getCreationDate(), courseDTOS.getContent().get(1).getCreationDate());
        assertEquals(courseTwo.getUpdateDate(), courseDTOS.getContent().get(1).getUpdateDate());

        verify(courseRepository, times(1)).findAll(spec, pageable);
    }

    @DisplayName("Given UserId Valid When Calling FindById Then Return CourseDTO Successfully")
    @Test
    void givenUserIdValid_WhenCallingFindById_ThenReturnCourseDTOSuccessfully() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseOne));
        CourseDTO courseDTO = courseService.findById(courseId);

        assertNotNull(courseDTO);
        assertEquals(courseRequestOne.getName(), courseDTO.getName());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @DisplayName("Given Invalid UserId When Calling FindById Then Throw CourseNotFoundException")
    @Test
    void givenInvalidUserId_WhenCallingFindById_ThenThrowCourseNotFoundException() {
        String expectedMessage = "There is no course registered with UUID " + courseId;
        when(courseRepository.findById(courseId)).thenThrow(new CourseNotFoundException(courseId));

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class,
                () -> courseService.findById(courseId));

        assertEquals(expectedMessage, exception.getMessage());
        verify(courseRepository, times(1)).findById(courseId);
    }

    @DisplayName("Given CourseRequest When Calling Save Then Return CourseDTO Successfully")
    @Test
    void givenCourseRequest_WhenCallingSave_ThenReturnCourseDTOSuccessfully() {
        when(courseRepository.save(any(Course.class))).thenReturn(courseOne);

        CourseDTO courseDTO = courseService.save(courseRequestOne);

        assertNotNull(courseDTO);
        assertEquals(courseRequestOne.getName(), courseDTO.getName());
        assertEquals(courseRequestOne.getDescription(), courseDTO.getDescription());
        assertEquals(courseRequestOne.getImageUrl(), courseDTO.getImageUrl());
        assertEquals(courseRequestOne.getCourseStatus(), courseDTO.getCourseStatus());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Given an invalid CourseRequest, When save is called, Then the repository save operation should never be invoked")
    void givenInvalidCourseRequest_WhenCallingSave_ThenRepositorySaveIsNeverCalled() {
        CourseRequest invalidCourseRequest = CourseRequest.builder()
                .name("Joel")
                .description("Test decription")
                .courseStatus(null)
                .userInstructor(null)
                .courseLevel(null)
                .build();
        verify(courseRepository, never()).save(any(Course.class));
    }

    @DisplayName("Given a CourseRequest and Valid CourseId When Calling Update Then It Should Update CourseDTO Successfully")
    @Test
    void givenCourseRequestAndValidCourseId_WhenCallingUpdate_ThenItShouldUpdateCourseDTOSuccessfully() {
        CourseRequest courseRequest = buildCourseRequest("Update course test",
                "update description", "http://updateimage.com");

        Course courseUpdate = initCourseDates(CourseRequest.toEntity(courseRequest), 5, 1);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseOne));
        when(courseRepository.save(any(Course.class))).thenReturn(courseUpdate);

        CourseDTO courseDTO = courseService.update(courseId, courseRequest);

        assertNotNull(courseDTO);
        assertEquals(courseUpdate.getName(), courseDTO.getName());
        assertEquals(courseUpdate.getDescription(), courseDTO.getDescription());
        assertEquals(courseUpdate.getImageUrl(), courseDTO.getImageUrl());
        assertEquals(courseUpdate.getCourseStatus(), courseDTO.getCourseStatus());
        assertEquals(courseUpdate.getCourseLevel(), courseDTO.getCourseLevel());

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @DisplayName("Given Valid CourseId When Calling Delete Then It Should Delete Course Successfully")
    @Test
    void givenValidCourseId_WhenCallingDelete_ThenItShouldDeleteCourseSuccessfully() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseOne));

        courseService.delete(courseId);

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).delete(courseOne);
    }

    @DisplayName("Given Invalid CourseId When Calling Delete Then It Should Throw CourseNotFoundException")
    @Test
    void givenInvalidCourseId_WhenCallingDelete_ThenItShouldThrowCourseNotFoundException() {
        UUID invalidCourseId = UUID.randomUUID();
        when(courseRepository.findById(invalidCourseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.delete(invalidCourseId));
        verify(courseRepository, times(1)).findById(invalidCourseId);
        verify(courseRepository, never()).delete(any(Course.class));
    }

    private CourseRequest buildCourseRequest(String name, String description, String imageUrl) {
        return CourseRequest.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .courseStatus(CourseStatus.INPROGRESS)
                .courseLevel(CourseLevel.BEGINNER)
                .build();
    }

    private Course initCourseDates(Course course, long creationDaysAgo, long updateDaysAgo) {
        course.setCreationDate(LocalDateTime.now().minusDays(creationDaysAgo));
        course.setUpdateDate(LocalDateTime.now().minusDays(updateDaysAgo));
        return course;
    }

    private Specification<Course> createCourseSpecification() {
        return new SpecificationTemplate.CourseSpec() {
            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("courseLevel"), "SomeLevel"));
                predicates.add(criteriaBuilder.equal(root.get("courseStatus"), "SomeStatus"));
                predicates.add(criteriaBuilder.like(root.get("name"), "%SomeName%"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

}