package com.ead.course.domain.services;

import com.ead.course.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

    Page<User> findAll(Specification<User> and, Pageable pageable);
}
