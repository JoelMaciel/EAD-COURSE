package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.domain.exceptions.CourseNotFoundException;
import com.ead.course.domain.exceptions.ModuleIntoCourseNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.domain.services.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceImplTest {

    public static final String MSG_COURSE_NOT_FOUND_IN_MODULE =
            "There is no course registered with UUID %s to module whit UUD %s";
    private static final String MSG_COURSE_NOT_FOUND = "There is no course registered with UUID %s";
    @InjectMocks
    private ModuleServiceImpl moduleService;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private CourseService courseService;

    private UUID courseId;
    private UUID moduleId;
    private Module moduleOne;
    private Module moduleTwo;
    private Course course;
    private ModuleRequest moduleRequest;
    private ModuleRequest moduleUpdateRequest;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        moduleId = UUID.randomUUID();

        course = new Course();

        moduleOne = Module.builder()
                .moduleId(moduleId)
                .title("Test Title")
                .description("Test Description")
                .creationDate(LocalDateTime.now())
                .course(course)
                .build();

        moduleTwo = Module.builder()
                .moduleId(moduleId)
                .title("Test Title")
                .description("Test Description")
                .creationDate(LocalDateTime.now())
                .course(course)
                .build();

        moduleRequest = ModuleRequest.builder()
                .title("Test Title")
                .description("Test Description")
                .build();

        moduleUpdateRequest = ModuleRequest.builder()
                .title("Update Title")
                .description("Update Description")
                .build();
    }

    @Test
    @DisplayName("Given Modules Exist When Calling FindAllByCourse Then Return List of Modules Successfully")
    void givenModulesExist_WhenCallingFindAllByCourse_ThenReturnListOfModules() {
        List<Module> modules = Arrays.asList(moduleOne, moduleTwo);
        when(moduleRepository.findByCourseCourseId(courseId)).thenReturn(modules);

        List<ModuleDTO> result = moduleService.findAllModulesByCourse(courseId);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(mod -> mod.getModuleId().equals(moduleOne.getModuleId())));
        assertTrue(result.stream().anyMatch(mod -> mod.getTitle().equals(moduleOne.getTitle())));

        assertTrue(result.stream().anyMatch(mod -> mod.getModuleId().equals(moduleTwo.getModuleId())));
        assertTrue(result.stream().anyMatch(mod -> mod.getTitle().equals(moduleTwo.getTitle())));
    }

    @Test
    @DisplayName("Given Module Exists When Calling FindByModule Then Return Module Successfully")
    void givenModuleExists_WhenCallingFindByModule_ThenReturnModuleSuccessfully() {
        when(moduleRepository.findByCourseCourseIdAndModuleId(courseId, moduleId)).thenReturn(Optional.of(moduleOne));

        ModuleDTO moduleDTO = moduleService.findByModule(courseId, moduleId);

        assertNotNull(moduleDTO);
        assertEquals(moduleOne.getModuleId(), moduleDTO.getModuleId());
        assertEquals(moduleOne.getTitle(), moduleDTO.getTitle());
        assertEquals(moduleOne.getDescription(), moduleDTO.getDescription());
    }

    @Test
    @DisplayName("Given Module Doesn't Exist When Calling FindByModule Then Throw Exception")
    void givenModuleDoesntExist_WhenCallingFindByModule_ThenThrowException() {
        when(moduleRepository.findByCourseCourseIdAndModuleId(courseId, moduleId)).thenReturn(Optional.empty());

        ModuleIntoCourseNotFoundException exception = assertThrows(ModuleIntoCourseNotFoundException.class,
                () -> moduleService.findByModule(courseId, moduleId));

        String expectedMessage = String.format(MSG_COURSE_NOT_FOUND_IN_MODULE, courseId, moduleId);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    @DisplayName("Given Valid ModuleRequest When Calling Save Then Return ModuleDTO Successfully")
    void givenValidRequest_WhenCallingSave_ThenReturnModuleDTO() {
        when(courseService.searchById(courseId)).thenReturn(course);
        when(moduleRepository.save(any(Module.class))).thenReturn(moduleOne);

        ModuleDTO moduleDTO = moduleService.save(courseId, moduleRequest);

        assertNotNull(moduleDTO);
        assertEquals(moduleOne.getModuleId(), moduleDTO.getModuleId());
        assertEquals(moduleOne.getTitle(), moduleDTO.getTitle());
    }

    @Test
    @DisplayName("Given Non-existent CourseId When Calling Save Then Throw Exception")
    void givenNonExistentCourseId_WhenCallingSave_ThenThrowException() {
        UUID invalidCourseId = UUID.randomUUID();
        when(courseService.searchById(invalidCourseId)).thenThrow(new CourseNotFoundException(invalidCourseId));

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class,
                () -> moduleService.save(invalidCourseId, moduleRequest));

        String expectedMessage = String.format(MSG_COURSE_NOT_FOUND, invalidCourseId);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Given Module Exists When Calling Update Then Return Updated ModuleDTO Successfully")
    void givenModuleExists_WhenCallingUpdate_ThenReturnUpdatedModuleDTO() {
        when(moduleRepository.findByCourseCourseIdAndModuleId(courseId, moduleId)).thenReturn(Optional.of(moduleOne));
        when(moduleRepository.save(any(Module.class))).thenAnswer(ModuleServiceImplTest::answer);

        ModuleDTO updateModuleDTO = moduleService.update(courseId, moduleId, moduleUpdateRequest);

        assertNotNull(updateModuleDTO);
        assertEquals(moduleOne.getModuleId(), updateModuleDTO.getModuleId());
        assertEquals(moduleUpdateRequest.getTitle(), updateModuleDTO.getTitle());
    }

    @Test
    @DisplayName("Given Module Does Not Exist When Calling Update Then Throw ModuleIntoCourseNotFoundException")
    void givenModuleDoesNotExist_WhenCallingUpdate_ThenThrowModuleIntoCourseNotFoundException() {
        when(moduleRepository.findByCourseCourseIdAndModuleId(courseId, moduleId)).thenReturn(Optional.empty());

        ModuleIntoCourseNotFoundException exception = assertThrows(ModuleIntoCourseNotFoundException.class,
                () -> moduleService.update(courseId, moduleId, moduleUpdateRequest));

        String expectedMessage = String.format(MSG_COURSE_NOT_FOUND_IN_MODULE, courseId, moduleId);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Given Module Exists When Calling Delete Then Succeed")
    void givenModuleExists_WhenCallingDelete_ThenSucceed() {
        when(moduleRepository.findByCourseCourseIdAndModuleId(courseId, moduleId)).thenReturn(Optional.of(moduleOne));

        moduleService.delete(courseId, moduleId);

        verify(moduleRepository, times(1)).findByCourseCourseIdAndModuleId(courseId, moduleId);
        verify(moduleRepository).delete(moduleOne);
        verifyNoMoreInteractions(moduleRepository);

    }

    @Test
    @DisplayName("Given Module Doesn't Exist When Calling Delete Then Throw Exception")
    void givenModuleDoesntExist_WhenCallingDelete_ThenThrowException() {
        when(moduleRepository.findByCourseCourseIdAndModuleId(courseId, moduleId))
                .thenThrow(new ModuleIntoCourseNotFoundException(courseId, moduleId));

        ModuleIntoCourseNotFoundException exception = assertThrows(ModuleIntoCourseNotFoundException.class,
                () -> moduleService.delete(courseId, moduleId));

        String expectedMessage = String.format(MSG_COURSE_NOT_FOUND_IN_MODULE, courseId, moduleId);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    private static Object answer(InvocationOnMock invocation) {
        return invocation.<Module>getArgument(0);
    }
}