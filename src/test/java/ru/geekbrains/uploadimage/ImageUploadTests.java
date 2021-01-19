package ru.geekbrains.uploadimage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.service.Endpoints;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ImageUploadTests extends BaseTest{

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
    void tearDown() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("data", is(true))
                .when()
                .delete(Endpoints.postImage, uploadedImageHashCode)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
