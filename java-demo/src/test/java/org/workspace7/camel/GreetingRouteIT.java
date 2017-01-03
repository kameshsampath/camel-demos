package org.workspace7.camel;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

/**
 * @author kameshs
 */
public class GreetingRouteIT {

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("http.port", 9090);
    }

    @Test
    public void testGreeting() throws Exception {

        given()
            .param("param1", "kamesh").
            when().
            get("/greeting").then()
            .assertThat().
            statusCode(200);
    }

    @Test
    public void testGreeting2() throws Exception {
        get("/greeting").then().assertThat().statusCode(204);
    }
}
