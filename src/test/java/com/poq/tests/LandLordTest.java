package com.poq.tests;

import com.poq.model.LandLord;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class LandLordTest {

    @BeforeClass
    public static void init() {
        baseURI = "http://localhost:8080";
    }

    @Test
    public void  getLandlords(){

       when()
                .get("/landlords")
       .then()
               .statusCode(200)
               .body("", not(empty()));
    }

    @Test
    public void postLandLord01(){

        LandLord landLord = new LandLord("Alex", "Fruz");

        //@formatter:off
        String id = given()
                .contentType(ContentType.JSON)
                .body(landLord)
        .when()
                .post("/landlords")
        .then()
                .statusCode(201)
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(false))
                .body("apartments", is(empty()))
        .extract()
                .path("id");
        given()
                .pathParam("id", id)
        .when()
                .get("/landlords/{id}")
        .then()
                .statusCode(200)
                .body("id", is(id))
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(false))
                .body("apartments", is(empty()));
    }

    @Test
    public void postLandLord02(){

        LandLord landLord = new LandLord("Alex", "Fruz", true);

        String id = given()
                .contentType(ContentType.JSON)
                .body(landLord)
        .when()
                .post("/landlords")
        .then()
                .statusCode(201)
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(true))
                .body("apartments", is(empty()))
        .extract()
                .path("id");
        given()
                .pathParam("id", id)
        .when()
                .get("/landlords/{id}")
        .then()
                .statusCode(200)
                .body("id", is(id))
                .body("firstName", is(landLord.getFirstName()))
                .body("lastName", is(landLord.getLastName()))
                .body("trusted", is(true))
                .body("apartments", is(empty()));
    }

    @Test
    public void postLandLordNegative01(){

        LandLord landLord = new LandLord("", "");

        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(landLord)
        .when()
                .post("/landlords")
        .then()
                .statusCode(400)
                .body("message", is("Fields are with validation errors"))
                .body("fieldErrorDTOs[0].fieldName", is("firstName"))
                .body("fieldErrorDTOs[0].fieldError", is("First name can not be empty"))
                .body("fieldErrorDTOs[1].fieldName", is("lastName"))
                .body("fieldErrorDTOs[1].fieldError", is("Last name can not be empty"));
    }
}
