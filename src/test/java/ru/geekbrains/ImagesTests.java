package ru.geekbrains;

import org.junit.jupiter.api.Test;
import ru.geekbrains.uploadimage.BaseTest;

import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ImagesTests extends BaseTest {


    @Test
    void getImageCountTest() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("data", is(0))
                .when()
                .get("/account/{username}/images/count", username)
                .then()
                .statusCode(200);

    }

    @Test
    void getImageCountClientAuthTest() {
        given()
                .headers(headers_cl)
                .expect()
                .body("success", is(false))
                .body("data.error", is("Unauthorized"))
                .when()
                .get("/account/{username}/images/count", username)
                .prettyPeek()
                .then()
                .statusCode(403);

    }

    @Test
    void getImageCountNoAuth() {
       given()
               .expect()
               .body("success", is(false))
               .body("data.error", is("Authentication required"))
               .when()
               .get("/account/{username}/images/count", username)
               .prettyPeek()
               .then()
               .statusCode(401);

    }

    @Test
    void getImageVerifyUrlTest() {
        String success = given()
                .headers(headers)
                .log()
                .uri()
                .when()
                .get("/account/{username}/images/count", username)
                .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .log()
                .status()
                .extract()
                .response()
                .jsonPath()
                .getString("success");
        assertThat(success, true);

    }

    @Test
    void getImageVerifyImageHashEmpty() {
        uploadedImageHashCode = "";
        given()
                .headers(headers)
                .log()
                .uri()
                .expect()
                .body("success", is(false))
                .body("data.error", is("An image ID is required for a GET request to /image"))
                .when()
                .get("image/{imageHash}", uploadedImageHashCode)
                .prettyPeek()
                .then()
                .statusCode(400)
                .contentType("application/json")
                .log()
                .status();

    }
}
