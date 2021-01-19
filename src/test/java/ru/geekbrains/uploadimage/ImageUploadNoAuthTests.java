package ru.geekbrains.uploadimage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

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
}
