package com.ead.course.domain.repositories;

import com.ead.course.domain.models.Lesson;
import com.ead.course.domain.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    Optional<Lesson> findByModuleModuleIdAndLessonId(UUID moduleId, UUID lessonId);

    List<Lesson> findByModule_ModuleId(UUID moduleId);

}
