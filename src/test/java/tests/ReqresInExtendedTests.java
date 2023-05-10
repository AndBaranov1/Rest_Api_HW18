package tests;


import models.lombok.CreateUserBodyLombokModel;
import models.lombok.CreateUserResponseLombokModel;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static models.specs.CreateUserSpec.createUserRequestSpec;
import static models.specs.CreateUserSpec.createUserResponseSpec;
import static models.specs.DeleteUserSpec.deleteUserRequestSpec;
import static models.specs.DeleteUserSpec.deleteUserResponseSpec;
import static models.specs.LoginSpec.errorloginResponseSpec;
import static models.specs.LoginSpec.loginRequestSpec;
import static models.specs.LoginSpec.loginResponseSpec;
import static models.specs.UpdateUserSpec.updateUserRequestSpec;
import static models.specs.UpdateUserSpec.updateUserResponseSpec;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReqresInExtendedTests {

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
    void reqresCreateUsersWithAllureSpecsTest() {
        CreateUserBodyLombokModel createUserBodyLombokModel = new CreateUserBodyLombokModel();
        createUserBodyLombokModel.setName("Ivan");
        createUserBodyLombokModel.setJob("QA_Automation");

        CreateUserResponseLombokModel responseLombokModel = step("Make request", () ->
                given(createUserRequestSpec)
                        .body(createUserBodyLombokModel)
                        .when()
                        .post()
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseLombokModel.class));

        step("Verify response Name", () ->
                assertThat(responseLombokModel.getName()).isEqualTo("Ivan"));

        step("Verify response Job", () ->
                assertThat(responseLombokModel.getJob()).isEqualTo("QA_Automation"));
    }

    @Test
    void reqresUpdateUsersWithAllureSpecsTest() {
        CreateUserBodyLombokModel updateUserBodyLombokModel = new CreateUserBodyLombokModel();
        updateUserBodyLombokModel.setName("Petr");
        updateUserBodyLombokModel.setJob("Developer");

        CreateUserResponseLombokModel responseLombokModel = step("Make request", () ->
                given(updateUserRequestSpec)
                        .body(updateUserBodyLombokModel)
                        .when()
                        .put()
                        .then()
                        .spec(updateUserResponseSpec)
                        .extract().as(CreateUserResponseLombokModel.class));

        step("Verify response Name", () ->
                assertThat(responseLombokModel.getName()).isEqualTo("Petr"));

        step("Verify response Job", () ->
                assertThat(responseLombokModel.getJob()).isEqualTo("Developer"));

    }

    @Test
    void unSuccessfulLoginUsersWithAllureSpecsTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("");

        LoginResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post()
                        .then()
                        .spec(errorloginResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify response", () ->
                assertThat(response.getError()).isEqualTo("Missing password"));
    }

    @Test
    void checkDeleteUser() {

        step("Make request", () -> given(deleteUserRequestSpec)
                .log().uri()
                .when()
                .delete()
                .then()
                .spec(deleteUserResponseSpec));
    }
}

