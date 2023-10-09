package com.ead.course.domain.repositories;

import com.ead.course.domain.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {

    @Query(value = "select * from module where course_id = :courseId", nativeQuery = true)
    List<Module> findAllModulesIntoCourse(@Param("courseId") UUID courseId);
}
