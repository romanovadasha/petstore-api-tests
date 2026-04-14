package config;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpec {

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .build();
}
