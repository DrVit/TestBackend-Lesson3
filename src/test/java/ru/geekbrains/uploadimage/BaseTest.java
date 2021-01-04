package ru.geekbrains.uploadimage;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import ru.geekbrains.ImagesTests;
import ru.geekbrains.RunJUnitTests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseTest {
    protected static Properties properties = new Properties();
    protected static String token;
    protected static String clientid;
    protected static String username;
    protected static Map<String, String> headers = new HashMap<>();
    protected static Map<String, String> headers_cl = new HashMap<>();
    protected String encodedImage;
    protected String uploadedImageHashCode;
    protected String uploadedImageHashCode1;

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = properties.getProperty("token");
        clientid = properties.getProperty("clientid");
        username = properties.getProperty("username");
        headers.put("Authorization", token);
        headers_cl.put("Authorization", clientid);

        RestAssured.baseURI = properties.getProperty("base.url");
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private static void loadProperties() {
        try {
            properties.load(new FileInputStream("src/test/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
