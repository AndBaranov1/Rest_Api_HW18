package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyPojoModel;
import models.pojo.LoginResponsePojoModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static models.specs.LoginSpec.loginRequestSpec;
import static models.specs.LoginSpec.loginResponseSpec;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

public class RestTests {

    @Test
    void reqresCreateUsersTest() {
        String body = "{ \"name\": \"Ivan\", \"job\": \"QA_Automation\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Ivan"))
                .body("job", is("QA_Automation"));

    }

    @Test
    void reqresUpdateUsersTest() {
        String body = "{ \"name\": \"Petr\", \"job\": \"Developer\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("Petr"))
                .body("job", is("Developer"));
    }

    @Test
    void reqresUnSuccessfulLoginUsersTest() {
        String requestBody = "{\"email\": \"test@mail.ru\", \"password\": \"\" }";
        given()
                .log().uri()
                .body(requestBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));

    }

    @Test
    void reqresSuccessfulLoginUsersTest() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                .log().uri()
                .body(requestBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/successfulLogin.json"));
    }

    @Test
    void successfulLoginWithPojoTest() {
//        String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        LoginBodyPojoModel loginBody = new LoginBodyPojoModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponsePojoModel response = given()
                .log().uri()
                .body(loginBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponsePojoModel.class);
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void successfulLoginWithLombokTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel response = given()
                .log().uri()
                .body(loginBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void successfulLoginWithAllureTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel response = given()
                .filter(new AllureRestAssured())
                .log().uri()
                .body(loginBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void successfulLoginWithCustomAllureTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel response = given()
                .filter(withCustomTemplates())
                .log().uri()
                .body(loginBody)
                .contentType(JSON)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void successfulLoginWithAllureStepsTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Make request", () ->
                given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .body(loginBody)
                        .contentType(JSON)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify response", () ->
                assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void successfulLoginWithAllureSpecsTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify response", () ->
                assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @ParameterizedTest()
    @CsvSource(value = {"eve.holt@reqres.in:cityslicka", "tEst:test", "Java:java"}, delimiter = ':')
    void successfulLoginParametrizedTest(String login, String password) {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail(login);
        loginBody.setPassword(password);

        LoginResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post()
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify response", () ->
                assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void reqresSingleUserNotFoundTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }
}