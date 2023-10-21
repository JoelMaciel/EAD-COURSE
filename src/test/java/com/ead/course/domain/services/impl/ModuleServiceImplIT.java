package com.ead.course.domain.services.impl;

import com.ead.course.api.dtos.request.ModuleRequest;
import com.ead.course.api.dtos.response.ModuleDTO;
import com.ead.course.api.specification.SpecificationTemplate;
import com.ead.course.containers.DatabaseContainerConfiguration;
import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.ead.course.domain.exceptions.ModuleIntoCourseNotFoundException;
import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.Module;
import com.ead.course.domain.repositories.CourseRepository;
import com.ead.course.domain.repositories.ModuleRepository;
import com.ead.course.utils.TestHelper;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Import(DatabaseContainerConfiguration.class)
@ActiveProfiles("test")
class ModuleServiceImplIT {
    public static final String MSG_COURSE_NOT_FOUND_IN_MODULE =
            "There is no course registered with UUID %s to module whit UUD %s";
    @Autowired
    private ModuleServiceImpl moduleService;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    private ModuleRequest moduleRequest;
    private ModuleRequest moduleRequestUpdate;
    private Module module;
    private Module secondModule;
    private Course course;


    @BeforeEach
    void setUp() {
        moduleRequest = TestHelper.createModuleRequest(
                "Module Test", "Test description.");
        moduleRequestUpdate = TestHelper.createModuleRequest(
                "Title Update", "This is Update description.");

        course = TestHelper.createCourse(
                "Course Test " + UUID.randomUUID(),
                "Test Description One",
                "http://testimage1.com",
                CourseStatus.INPROGRESS,
                UUID.randomUUID(),
                CourseLevel.BEGINNER);

        courseRepository.save(course);

        module = TestHelper.createModule(moduleRequest, course);

        secondModule = TestHelper.createModule(
                TestHelper.createModuleRequest(
                        "Second Module Test", "Second test description."), course
        );

        moduleRepository.save(module);
        moduleRepository.save(secondModule);
    }


    @AfterEach
    void tearDown() {
        moduleRepository.deleteAll();
    }

    @Test
    @DisplayName("Given Course ID When Finding Modules Then Return Page Of ModuleDTOs")
    void givenCourseId_WhenFindingModules_ThenReturnPageOfModuleDTOs() {
        UUID courseId = course.getCourseId();
        Specification<Module> spec = SpecificationTemplate.moduleCourseId(courseId);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("moduleId"));

        Page<ModuleDTO> moduleDTOPage = moduleService.findAllModules(spec, pageable);

        assertNotNull(moduleDTOPage);
        assertFalse(moduleDTOPage.isEmpty());
        assertEquals(2, moduleDTOPage.getTotalElements());

        List<String> titles = moduleDTOPage.stream()
                .map(ModuleDTO::getTitle)
                .collect(Collectors.toList());

        assertTrue(titles.contains(moduleRequest.getTitle()));
        assertTrue(titles.contains("Second Module Test"));

        List<String> descriptions = moduleDTOPage.stream()
                .map(ModuleDTO::getDescription)
                .collect(Collectors.toList());

        assertTrue(descriptions.contains(moduleRequest.getDescription()));
        assertTrue(descriptions.contains("Second test description."));
    }

    @Test
    @DisplayName("Given Module Request When Saving Module Then Return Saved ModuleDTO")
    void givenModuleRequest_WhenSaving_ThenReturnSavedModuleDTO() {
        ModuleDTO moduleDTO = moduleService.save(course.getCourseId(), moduleRequest);

        assertNotNull(moduleDTO);
        assertEquals(moduleRequest.getDescription(), moduleDTO.getDescription());
        assertEquals(moduleRequest.getTitle(), moduleDTO.getTitle());
    }

    @Test
    @DisplayName("Given Course and Module ID When Finding Module Then Return ModuleDTO")
    void givenCourseAndModuleId_WhenFindingModule_ThenReturnModuleDTO() {
        ModuleDTO moduleDTO = moduleService.findByModule(course.getCourseId(), module.getModuleId());

        assertNotNull(moduleDTO);
        assertEquals(module.getModuleId(), moduleDTO.getModuleId());
        assertEquals(module.getTitle(), moduleDTO.getTitle());
    }

    @Test
    @DisplayName("Given Invalid Course ID and Valid Module ID When Finding Module Then Should Throw Exception")
    void givenInvalidCourseIdAndModuleId_WhenFindingModule_ThenThrowException() {
        UUID invalidCourseId = UUID.randomUUID();

        ModuleIntoCourseNotFoundException exception = assertThrows(ModuleIntoCourseNotFoundException.class,
                () -> moduleService.findByModule(invalidCourseId, module.getModuleId()));

        String expectedMessage = String.format(
                MSG_COURSE_NOT_FOUND_IN_MODULE, invalidCourseId, module.getModuleId());

        assertEquals(expectedMessage, exception.getMessage());

    }


    @Test
    @DisplayName("Given Module Exists When Calling Update Then Return Updated ModuleDTO Successfully")
    void givenModuleExists_WhenCallingUpdate_ThenReturnUpdatedModuleDTO() {
        ModuleDTO moduleDTO = moduleService.update(course.getCourseId(), module.getModuleId(), moduleRequestUpdate);

        assertNotNull(moduleDTO);
        assertEquals(moduleRequestUpdate.getTitle(), moduleDTO.getTitle());
        assertEquals(moduleRequestUpdate.getDescription(), moduleDTO.getDescription());
    }

    @Test
    @DisplayName("Given Module Does Not Exist When Calling Update Then Test Should Fail")
    void givenModuleDoesNotExist_WhenCallingUpdate_ThenTestShouldFail() {
        UUID invalidModuleId = UUID.randomUUID();

        ModuleIntoCourseNotFoundException exception = assertThrows(ModuleIntoCourseNotFoundException.class,
                () -> moduleService.update(course.getCourseId(), invalidModuleId, moduleRequestUpdate));

        String expectedMessage = String.format(
                MSG_COURSE_NOT_FOUND_IN_MODULE, course.getCourseId(), invalidModuleId);

        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Given Module Exists When Deleting, Ensure Module is Deleted")
    void givenModuleExists_WhenDeleting_ThenEnsureModuleIsDeleted() {
        long initialCount = moduleRepository.count();
        moduleService.delete(course.getCourseId(), module.getModuleId());

        long afterDeleteCount = moduleRepository.count();
        assertEquals(initialCount - 1, afterDeleteCount);

        assertFalse(moduleRepository.findById(module.getModuleId()).isPresent());
    }

    @Test
    @DisplayName("Given Module Does Not Exist When Deleting, Should Throw ModuleIntoCourseNotFoundException with Correct Message")
    void givenModuleDoesNotExist_WhenDeleting_ShouldThrowExceptionWithCorrectMessage() {
        UUID invalidModuleId = UUID.randomUUID();

        ModuleIntoCourseNotFoundException exception = assertThrows(ModuleIntoCourseNotFoundException.class,
                () -> moduleService.delete(course.getCourseId(), invalidModuleId));

        String expectedMessage = String.format(
                MSG_COURSE_NOT_FOUND_IN_MODULE, course.getCourseId(), invalidModuleId);

        assertEquals(exception.getMessage(), expectedMessage);
    }
}


