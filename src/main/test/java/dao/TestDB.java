package java.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MySQLContainer;
import utils.ConnectionPool;
import utils.PropertiesUtils;

import java.util.Properties;

public abstract class TestDB {
    private final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.30").withInitScript("script.sql");

    @BeforeEach
    void startContainer() {

        container.start();
        Properties properties = new Properties();
        properties.put("db.url", container.getJdbcUrl());
        properties.put("db.username", container.getUsername());
        properties.put("db.password", container.getPassword());
        PropertiesUtils.setProperties(properties);
        if (!ConnectionPool.getConnectionFlag()) {
            ConnectionPool.initConnectionPool();
        }
    }

    @AfterEach
    void close() {
        ConnectionPool.closePool();
        container.stop();
    }
}