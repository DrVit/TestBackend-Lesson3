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

public class ImageUploadNoAuthTests extends BaseTest{

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);
    }

    @Test
    void uploadFileTest() {
        uploadedImageHashCode = given()
                .multiPart("image", encodedImage)
                .expect()
                .body("success", is(false))
                .body("data.error", is("Authentication required"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(401)
        .extract()
        .response()
        .jsonPath()
        .getString("data.deletehash");

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
