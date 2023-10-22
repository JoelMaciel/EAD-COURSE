package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.LessonRequest;
import com.ead.course.api.dtos.response.LessonDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.containers.DatabaseContainerConfiguration;
import com.ead.course.domain.exceptions.LessonIntoModuleNotFoundException;
import com.ead.course.domain.models.Lesson;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
import com.ead.course.domain.services.ModuleService;
import org.junit.Ignore;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Import(DatabaseContainerConfiguration.class)
@ActiveProfiles("test")
class LessonServiceImplIT {

    public static final String MSG_MODULE_COURSE_NOT_FOUND =
            "There is no module registered with UUID %s to lesson whit UUID %s";
    @Autowired
    private LessonServiceImpl lessonService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseService courseService;
    @Autowired
    private ModuleService moduleService;
    private UUID lessonId;
    private UUID lessonIdTwo;
    private UUID moduleId;
    private UUID moduleIdTwo;
    private Pageable pageable;
    private LessonRequest lessonRequest;
    private Specification<Lesson> moduleIdSpec;
    private Specification<Lesson> titleLikeSpec;

    @BeforeEach
    void setUp() {
        moduleId = UUID.fromString("0e7f73ed-38ed-4050-ac8d-de0195e60862");
        moduleIdTwo = UUID.fromString("7a2ec76c-4d7c-463a-836a-18a55b5272df");
        lessonId = UUID.fromString("c9a8ed0a-423d-4e99-ac53-dcc5174113a1");
        lessonIdTwo = UUID.fromString("adcbb827-223c-496a-9e7d-5eaa13ccc725");


        lessonRequest = new LessonRequest("Titulo", "Descrição", "url.com/video");

        pageable = PageRequest.of(0, 10, Sort.by("lessonId"));
        moduleIdSpec = SpecificationTemplate.lessonModuleId(moduleId);
        titleLikeSpec = (root, query, cb) ->
                cb.like(root.get("title"), "%Java History%");

    }

    @Test
    @DisplayName("Given valid specifications When Finding All Lessons Then Return Page of LessonDTOs")
    void givenValidSpecifications_WhenFindingAllLessons_ThenReturnPageOfLessonDTOs() {
        lessonService.save(moduleId, lessonRequest);
        Page<LessonDTO> lessonDTOS = lessonService.findAll(moduleIdSpec, pageable);

        assertFalse(lessonDTOS.isEmpty());
        assertEquals(3, lessonDTOS.getTotalElements());
    }

    @Test
    @DisplayName("Given valid specifications When Finding All Lessons by Title Then Return Page of LessonDTOs")
    void givenValidSpecifications_WhenFindingAllLessonsByTitle_ThenReturnPageOfLessonDTOs() {
        String titleExpected = "Java History";
        Page<LessonDTO> lessonDTOS = lessonService.findAll(titleLikeSpec, pageable);

        assertFalse(lessonDTOS.isEmpty());
        LessonDTO lessonDTO = lessonDTOS.getContent().get(0);
        assertEquals(lessonDTO.getTitle(), titleExpected);
    }

    @Test
    @DisplayName("Given valid moduleId and lessonRequest When Saving Lesson Then Return LessonDTO")
    void givenValidModuleIdAndLessonRequest_WhenSavingLesson_ThenReturnLessonDTO() {
        LessonDTO lessonDTO = lessonService.save(moduleId, lessonRequest);

        assertNotNull(lessonDTO);
        assertEquals(lessonRequest.getTitle(), lessonDTO.getTitle());
        assertEquals(lessonRequest.getDescription(), lessonDTO.getDescription());
    }

    @Ignore
    @Test
    @DisplayName("Given valid moduleId, lessonId, and lessonRequest When Updating Lesson Then Return Updated LessonDTO")
    void givenValidModuleIdAndLessonIdAndLessonRequest_WhenUpdatingLesson_ThenReturnUpdatedLessonDTO() {
        Optional<Module> module = moduleRepository.findById(moduleIdTwo);
        Optional<Lesson> lesson = lessonRepository.findById(lessonIdTwo);
        LessonRequest updateRequest = new LessonRequest("Updated Title", "Updated Description", "updated.url.com/video");
        LessonDTO updatedLessonDTO = lessonService.update(
                module.get().getModuleId(), lesson.get().getLessonId(), updateRequest);

        assertNotNull(updatedLessonDTO);
        assertEquals(updateRequest.getTitle(), updatedLessonDTO.getTitle());
        assertEquals(updateRequest.getDescription(), updatedLessonDTO.getDescription());
        assertEquals(updateRequest.getVideoUrl(), updatedLessonDTO.getVideoUrl());
    }

    @Test
    @DisplayName("Given invalid moduleId or lessonId When Finding Lesson by Id Then Throw Exception")
    void givenInvalidModuleIdOrLessonId_WhenFindingLessonById_ThenThrowException() {
        LessonIntoModuleNotFoundException exception = assertThrows(LessonIntoModuleNotFoundException.class,
                () -> lessonService.findById(moduleId, lessonId)
        );

        String expectedMessage = String.format(MSG_MODULE_COURSE_NOT_FOUND, moduleId, lessonId);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Given valid moduleId and lessonId When Deleting Lesson Then Lesson Should Be Deleted")
    void givenValidModuleIdAndLessonId_WhenDeletingLesson_ThenLessonShouldBeDeleted() {
        long initialLessonCount = lessonRepository.count();
        lessonService.delete(moduleId, lessonId);

        long afterDeleteCount = lessonRepository.count();
        assertEquals(initialLessonCount - 1, afterDeleteCount);

        assertFalse(lessonRepository.findById(lessonId).isPresent());
        assertTrue(moduleRepository.existsById(moduleId));
    }

    @Test
    @DisplayName("Given invalid moduleId or lessonId When Deleting Lesson Then Throw Exception")
    void givenInvalidModuleIdOrLessonId_WhenDeletingLesson_ThenThrowException() {
        LessonIntoModuleNotFoundException exception = assertThrows(LessonIntoModuleNotFoundException.class,
                () -> lessonService.delete(moduleId, lessonId));

        String expectedMessage = String.format(MSG_MODULE_COURSE_NOT_FOUND, moduleId, lessonId);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}