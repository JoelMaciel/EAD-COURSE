package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.LessonRequest;
import com.ead.course.api.dtos.response.LessonDTO;
import com.ead.course.domain.exceptions.LessonIntoModuleNotFoundException;
import com.ead.course.domain.exceptions.ModuleNotFoundException;
import com.ead.course.domain.models.Lesson;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.LessonRepository;
import com.ead.course.domain.services.ModuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    public static final String MSG_MODULE_IN_COURSE_NOT_FOUND =
            "There is no module registered with UUID %s to lesson whit UUID %s";
    public static final String MSG_MODULE_NOT_FOUND = "There is no module registered with UUID %s";
    @InjectMocks
    private LessonServiceImpl lessonService;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private ModuleService moduleService;

    private UUID lessonId;
    private UUID moduleId;
    private UUID invalidModuelId;
    private Lesson lessonOne;
    private Lesson lessonTwo;
    private Pageable pageable;
    private LessonRequest lessonRequest;

    @BeforeEach
    void setUp() {
        lessonId = UUID.randomUUID();
        invalidModuelId = UUID.randomUUID();

        Module mockModule = new Module();
        mockModule.setModuleId(UUID.randomUUID());
        moduleId = mockModule.getModuleId();

        lessonOne = Lesson.builder()
                .lessonId(lessonId)
                .title("Java Title 1")
                .description("descOne")
                .videoUrl("http://video.testOne")
                .creationDate(LocalDateTime.now())
                .build();
        lessonOne.setModule(mockModule);

        lessonTwo = Lesson.builder()
                .lessonId(UUID.randomUUID())
                .title("Java Title 2")
                .description("descTwo")
                .videoUrl("http://video.testTwo")
                .creationDate(LocalDateTime.now())
                .build();
        lessonTwo.setModule(mockModule);

        lessonRequest = LessonRequest.builder()
                .title("Java Title 1")
                .description("descOne")
                .videoUrl("http://video.request")
                .build();

        pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "lessonId"));
    }

    @Test
    @DisplayName("Given a Page of Lessons When Calling FindAll Then It Should PageDTO Lessons Successfully")
    void givenPageOfLessons_WhenCallingFindAll_ThenShouldReturnPageDTOLessonsSuccessfully() {
        List<Lesson> lessons = Arrays.asList(lessonOne, lessonTwo);
        Page<Lesson> lessonPage = new PageImpl<>(lessons);

        Specification<Lesson> titleSpec = getTitleSpec("Java");
        when(lessonRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(lessonPage);

        Page<LessonDTO> result = lessonService.findAll(titleSpec, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getTotalElements());

        Assertions.assertTrue(result.getContent().stream().anyMatch(
                lessonDTO -> lessonDTO.getLessonId().equals(lessonOne.getLessonId())));

        Assertions.assertTrue(result.getContent().stream().anyMatch(
                lessonDTO -> lessonDTO.getTitle().equals(lessonOne.getTitle())));

        Assertions.assertTrue(result.getContent().stream().anyMatch(
                lessonDTO -> lessonDTO.getLessonId().equals(lessonTwo.getLessonId())));

        Assertions.assertTrue(result.getContent().stream().anyMatch(
                lessonDTO -> lessonDTO.getTitle().equals(lessonTwo.getTitle())));
    }

    private Specification<Lesson> getTitleSpec(String titleSpecif) {
        return (root, query, cb) -> cb.like(root.get("title"), "%" + titleSpecif + "%");
    }


    @Test
    @DisplayName("Given Lesson ID When Calling findById Then Return LessonDTO Successfully")
    void givenLessonId_WhenCallingFindById_ThenReturnLessonDTOSuccessfully() {
        when(lessonRepository.findByModuleModuleIdAndLessonId(
                eq(moduleId), eq(lessonId))).thenReturn(Optional.of(lessonOne));

        LessonDTO result = lessonService.findById(moduleId, lessonId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(lessonOne.getLessonId(), result.getLessonId());
        Assertions.assertEquals(lessonOne.getTitle(), result.getTitle());
    }

    @Test
    @DisplayName("Given Non-existent Lesson ID for a Module When Calling findById Then Throw Exception")
    void givenNonexistentLessonIdForModule_WhenCallingFindById_ThenThrowException() {
        when(lessonRepository.findByModuleModuleIdAndLessonId(
                eq(moduleId), eq(lessonId))).thenReturn(Optional.empty());

        LessonIntoModuleNotFoundException exception = assertThrows(LessonIntoModuleNotFoundException.class,
                () -> lessonService.findById(moduleId, lessonId));

        String expectedMessage = String.format(MSG_MODULE_IN_COURSE_NOT_FOUND, moduleId, lessonId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Given New Lesson When Calling Save Then Return Saved LessonDTO Successfully")
    void givenNewLesson_WhenCallingSave_ThenReturnSavedLessonDTOSuccessfully() {
        when(moduleService.searchByModule(moduleId)).thenReturn(new Module());
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lessonOne);

        LessonDTO lessonDTO = lessonService.save(moduleId, new LessonRequest());

        assertNotNull(lessonDTO);
        assertEquals(lessonOne.getLessonId(), lessonDTO.getLessonId());
        assertEquals(lessonOne.getTitle(), lessonDTO.getTitle());

    }

    @Test
    @DisplayName("Given Invalid ModuleId When Calling save Then Throw ModuleNotFoundException")
    void givenInvalidModuleId_WhenCallingSave_ThenThrowModuleNotFoundException() {
        when(moduleService.searchByModule(eq(invalidModuelId)))
                .thenThrow(new ModuleNotFoundException(invalidModuelId));

        ModuleNotFoundException exception = assertThrows(ModuleNotFoundException.class, () -> {
            lessonService.save(invalidModuelId, new LessonRequest());
        });

        String actualMessage = String.format(MSG_MODULE_NOT_FOUND, invalidModuelId);
        String expectedeMessage = exception.getMessage();

        assertEquals(actualMessage, expectedeMessage);
    }


    @Test
    @DisplayName("Given Valid ModuleId, LessonId and LessonRequest When Calling update Then Return Updated LessonDTO Successfully")
    void givenValidModuleIdLessonIdAndLessonRequest_WhenCallingUpdate_ThenReturnUpdatedLessonDTOSuccessfully() {
        when(lessonRepository.findByModuleModuleIdAndLessonId(eq(moduleId), eq(lessonId)))
                .thenReturn(Optional.of(lessonOne));

        when(lessonRepository.save(any(Lesson.class))).thenReturn(lessonOne);

        LessonDTO result = lessonService.update(moduleId, lessonId, lessonRequest);

        assertNotNull(result);
        assertEquals(lessonOne.getLessonId(), result.getLessonId());
        assertEquals(lessonOne.getTitle(), result.getTitle());
    }

    @Test
    @DisplayName("Given Invalid ModuleId or LessonId When Calling update Then Throw LessonIntoModuleNotFoundException")
    void givenInvalidModuleIdOrLessonId_WhenCallingUpdate_ThenThrowLessonIntoModuleNotFoundException() {
        when(lessonRepository.findByModuleModuleIdAndLessonId(eq(invalidModuelId), eq(lessonId)))
                .thenReturn(Optional.empty());

        LessonIntoModuleNotFoundException exception = assertThrows(LessonIntoModuleNotFoundException.class,
                () -> lessonService.update(invalidModuelId, lessonId, lessonRequest));

        String expectedMessage = String.format(MSG_MODULE_IN_COURSE_NOT_FOUND, invalidModuelId, lessonId);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Given Valid ModuleId and LessonId When Calling delete Then Lesson Is Deleted Successfully")
    void givenValidModuleIdAndLessonId_WhenCallingDelete_ThenLessonIsDeletedSuccessfully() {
        when(lessonRepository.findByModuleModuleIdAndLessonId(eq(moduleId), eq(lessonId)))
                .thenReturn(Optional.of(lessonOne));
        doNothing().when(lessonRepository).delete(lessonOne);

        lessonService.delete(moduleId, lessonId);

        verify(lessonRepository).findByModuleModuleIdAndLessonId(eq(moduleId), eq(lessonId));
        verify(lessonRepository).delete(lessonOne);
        verifyNoMoreInteractions(lessonRepository);

    }

    @Test
    @DisplayName("Given Invalid ModuleId and LessonId When Calling delete Then Throw Exception")
    void givenInvalidModuleIdAndLessonId_WhenCallingDelete_ThenThrowException() {
        when(lessonRepository.findByModuleModuleIdAndLessonId(eq(invalidModuelId), eq(lessonId)))
                .thenReturn(Optional.empty());

        LessonIntoModuleNotFoundException exception = assertThrows(LessonIntoModuleNotFoundException.class,
                () -> lessonService.delete(invalidModuelId, lessonId));

        String expectedMessage = String.format(MSG_MODULE_IN_COURSE_NOT_FOUND, invalidModuelId, lessonId);
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
        verify(lessonRepository, never()).delete(any(Lesson.class));
    }
}
