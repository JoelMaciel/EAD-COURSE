package com.ead.course.domain.repositories;

import com.ead.course.domain.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID>, JpaSpecificationExecutor<Course> {

    @Query(value = "select case when count(*) > 0 then 1 else 0 end from course_user tcu " +
            "where tcu.course_id = :courseId and tcu.user_id = :userId", nativeQuery = true)
    BigInteger existsByCourseAndUser(@Param("courseId") UUID courseId, @Param("userId") UUID userId);

}
