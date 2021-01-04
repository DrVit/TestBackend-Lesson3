package ru.geekbrains.uploadimage;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ImageUpdateTests extends BaseTest{

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);
    }

    @Test
    void uploadFileTest() {
        uploadedImageHashCode = given()
                .headers(headers)
                .multiPart("image", encodedImage)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
        .extract()
        .response()
        .jsonPath()
        .getString("data.deletehash");

    }
    @AfterEach
    void tearUpDate() {
        given()
                .headers(headers)
                .multiPart("title", "Minions")
                .multiPart("description", "Stupids around")
                .expect()
                .body("success", is(true))
                .when()
                .post("/image/{imageHash}", uploadedImageHashCode)
                .prettyPeek()
                .then()
                .statusCode(200);
        uploadedImageHashCode1 = uploadedImageHashCode;
    }

    @AfterEach
    void tearUpDown() {
                given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("data", is(true))
                .when()
                .delete("image/{imageHash}", uploadedImageHashCode1)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    private byte[] getFileContentInBase64() {
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
