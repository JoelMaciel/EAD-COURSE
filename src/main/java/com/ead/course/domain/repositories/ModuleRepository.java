package com.ead.course.domain.repositories;

import com.ead.course.domain.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {

    Optional<Module> findByCourseCourseIdAndModuleId(UUID courseId, UUID moduleId);

    List<Module> findByCourseCourseId(UUID courseId);


}
