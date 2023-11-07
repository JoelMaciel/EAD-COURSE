package com.ead.course.domain.repositories;

import com.ead.course.domain.models.Course;
import com.ead.course.domain.models.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, UUID>{

    boolean existsByCourseAndUserId(Course course, UUID userId);

    boolean existsByCourse_CourseId(UUID courseId);

    boolean existsByUserId(UUID userId);
    void deleteAllByUserId(UUID userId);
}
