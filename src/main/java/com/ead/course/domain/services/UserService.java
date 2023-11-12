package com.ead.course.domain.services;

import com.ead.course.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface UserService {

    Page<User> findAll(Specification<User> and, Pageable pageable);

    User save(User user);

    void delete(UUID userId);

    User searchById(UUID userId);

}
