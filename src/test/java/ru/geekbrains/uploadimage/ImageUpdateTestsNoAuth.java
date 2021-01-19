package ru.geekbrains.uploadimage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.service.Endpoints;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ImageUpdateTestsNoAuth extends BaseTest{

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
                .multiPart("title", "Minions")
                .multiPart("description", "Stupids around")
                .expect()
                .body("success", is(false))
                .body("data.error", is("Authentication required"))
                .when()
                .post(Endpoints.postImage, uploadedImageHashCode)
                .prettyPeek()
                .then()
                .statusCode(401);
        uploadedImageHashCode1 = uploadedImageHashCode;
    }

    @AfterEach
    void tearUpDown() {
                given()
                .headers(headers_cl)
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
