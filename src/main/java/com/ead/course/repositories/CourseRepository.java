package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Retention;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, UUID> , JpaSpecificationExecutor<CourseModel> {
}
