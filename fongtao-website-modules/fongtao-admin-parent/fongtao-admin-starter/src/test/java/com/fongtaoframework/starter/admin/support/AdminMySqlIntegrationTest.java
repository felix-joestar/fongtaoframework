package com.fongtaoframework.starter.admin.support;

import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AdminMySqlIntegrationTest {

    @Container
    protected static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("fongtao_admin_test")
            .withUsername("fongtao")
            .withPassword("fongtao");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.datasource.druid.validation-query", () -> "select 1");
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.schema-locations", () -> "file:../_database_schema/admin-mysql-schema.sql");
        registry.add("spring.sql.init.data-locations", () -> "classpath:admin-test-data.sql");
        registry.add("mybatis-plus.configuration.map-underscore-to-camel-case", () -> "true");
        registry.add("fongtao.security.jwt.secret", () -> "0123456789abcdef0123456789abcdef");
    }
}
