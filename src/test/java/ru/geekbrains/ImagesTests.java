package ru.geekbrains;

import org.junit.jupiter.api.Test;
import ru.geekbrains.service.Endpoints;
import ru.geekbrains.uploadimage.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ImagesTests extends BaseTest {

    @Test
    void getImageCountTest() {
        given()
                .headers(headers)
                .expect()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .body("success", is(true))
                .body("data", is(0))
                .when()
                .get(Endpoints.getImageUserAccountCount, username)
                .then()
                .spec(responseSpec);
//                .statusCode(200);

    }

    @Test
    void getImageCountClientAuthTest() {
        given()
                .headers(headers_cl)
                .expect()
                .body("success", is(false))
                .body("data.error", is("Unauthorized"))
                .when()
                .get(Endpoints.getImageUserAccountCount, username)
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
               .get(Endpoints.getImageUserAccountCount, username)
               .prettyPeek()
               .then()
               .statusCode(401);

    }

    @Test
    void getImageVerifyUrlTest() {
        String success = given()
                .spec(requestSpec)
                .expect()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .when()
                .get(Endpoints.getImageUserAccountCount, username)
                .prettyPeek()
                .then()
                .spec(responseSpec)
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
                .get(Endpoints.postImage, uploadedImageHashCode)
                .prettyPeek()
                .then()
                .statusCode(400)
                .contentType("application/json")
                .log()
                .status();

    }
}
