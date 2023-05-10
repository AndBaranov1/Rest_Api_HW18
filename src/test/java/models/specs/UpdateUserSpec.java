package models.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserSpec {
    public static RequestSpecification updateUserRequestSpec = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().body()
            .contentType(JSON)
            .baseUri("https://reqres.in")
            .basePath("/api/users/2");

    public static ResponseSpecification updateUserResponseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .expectBody("name", notNullValue())
            .expectBody("job", notNullValue())
            .build();
}