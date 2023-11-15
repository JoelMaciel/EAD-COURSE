package com.ead.course.containers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class DatabaseContainerConfiguration {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("ead_course_two_test")
            .withUsername("root")
            .withPassword("root")
            .withExposedPorts(3307);

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/ead_course_two_test",
                mysqlContainer.getHost(),
                mysqlContainer.getMappedPort(3307));

        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
}
