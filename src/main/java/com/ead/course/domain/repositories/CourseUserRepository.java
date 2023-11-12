package com.ead.course.domain.repositories;

import com.ead.course.domain.models.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, UUID> {
}
