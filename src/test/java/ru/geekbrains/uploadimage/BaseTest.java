package ru.geekbrains.uploadimage;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.Matchers.lessThan;

public abstract class BaseTest {
    protected static Properties properties = new Properties();
    protected static String token;
    protected static String clientid;
    protected static String username;
    protected static Map<String, String> headers = new HashMap<>();
    protected static Map<String, String> headers_cl = new HashMap<>();
    protected static String encodedImage;
    protected String uploadedImageHashCode;
    protected String uploadedImageHashCode1;
    protected String fileContent;
    protected static ResponseSpecification responseSpec = null;
    protected static RequestSpecification requestSpec = null;

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = properties.getProperty("token");
        clientid = properties.getProperty("clientid");
        username = properties.getProperty("username");
        headers.put("Authorization", token);
        headers_cl.put("Authorization", clientid);

        RestAssured.baseURI = properties.getProperty("base.url");

        requestSpec = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.JSON)
                .setAccept(ContentType.ANY)
                .log(LogDetail.ALL )
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(5000L))
                .build();
//        RestAssured.responseSpecification = responseSpec;

    }
    void setUp() {
        byte[] fileContent = getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);
    }

    private static void loadProperties() {
        try {
            properties.load(new FileInputStream("src/test/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected byte[] getFileContentInBase64() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(Objects.requireNonNull(classLoader.getResource("Minions.jpg")).getFile());
        byte[] fileContent = new byte[0];

        try {
            fileContent = FileUtils.readFileToByteArray(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

}
