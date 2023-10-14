package com.ead.course.integration.database;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class MySQLContainerInitializer {

    @Container
    public static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:5.7")
            .withDatabaseName("ead_course_test")
            .withUsername("root")
            .withPassword("root")
            .withExposedPorts(3306);

    static {
        mysqlContainer.start();
    }
}

