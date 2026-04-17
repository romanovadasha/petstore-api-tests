package tests;

import api.models.Pet;
import base.BaseTest;
import api.PetClient;
import api.models.PetBuilder;

import config.RequestSpec;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetContractTests extends BaseTest {

    private PetClient petClient = new PetClient();

    @Test
    void shouldNotReturnNameFieldWhenNull(){

        // Arrange
        Pet pet = new PetBuilder()
                .withName(null)
                .withId(System.currentTimeMillis())
                .withStatus("available")
                .build();

        // Act + Assert
        petClient.createPet(pet)
                .then()
                .statusCode(200)
                .body("$", not(hasKey("name")));

    }

    @Test
    void shouldSetDefaultNameWhenMissing(){

        //Arrange
        Pet pet = new PetBuilder()
                .withId(System.currentTimeMillis())
                .withStatus("available")
                .build();

        //Act + Asserts
        petClient.createPet(pet)
                .then()
                .statusCode(200)
                .body("name", notNullValue());
    }

    @Test
    void shouldHandleNullName(){

        //Arrange
        Pet pet = new PetBuilder()
                .withName(null)
                .build();

        //Act + Asserts
        petClient.createPet(pet)
                .then()
                .statusCode(200)
                .body("$", not(hasKey("name")));
    }

    @Test
    void shouldHandleMissingName(){

        //Arrange
        Pet pet = new PetBuilder()
                .withoutName()
                .build();

        //Act + Assert
        petClient.createPet(pet)
                .then()
                .statusCode(200)
                .body("$", not(hasKey("name")));
    }

    @Test
    void shouldHandleEmptyName(){

        //Arrange
        Pet pet = new PetBuilder()
                .withName("")
                .build();

        //Act
        Response response = petClient.createPet(pet);

        //Assert
        response.then()
                .statusCode(200)
                .body("$", hasKey("name"))
                .body("name", equalTo(""));
    }

    @Test
    void shouldHandleValidName(){

        //Arrange
        Pet pet = new PetBuilder()
                .withName("doggie")
                .build();

        //Act
        Response response = petClient.createPet(pet);

        //Assert
        response.then()
                .statusCode(200)
                .body("$", hasKey("name"))
                .body("name", equalTo("doggie"));
    }

    @Test
    void shouldHandleAnotherTypeName(){

        //Arrange
        String body = "{ \"name\": 123 }";

        //Act
        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        //.contentType("application/json")
                        .body(body)
                        .when()
                        .post("/pet");

        //Assert
        response.then()
                .statusCode(200)
                .body("$", hasKey("name"))
                .body("name", equalTo("123"));

    }

    @Test
    void shouldHandleTypeBooleanName(){

        //Arrange
        String body = "{ \"name\": true }";

        //Act
        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .body(body)
                        .when()
                        .post("/pet");

        //Assert
        response.then()
                .statusCode(200)
                .body("$", hasKey("name"))
                .body("name", equalTo("true"));
    }

    @Test
    void shouldHandleLongName(){

        //Arrange
        String longName = "a".repeat(1000);
        String body = "{ \"name\": \"" + longName + "\" }";

        //Act
        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .body(body)
                        .post("/pet");

        //System.out.println(response.jsonPath().getString("name").length());

        //Assert
        response.then()
                        .statusCode(200)
                                .body("$", hasKey("name"));

        assertEquals(1000, response.jsonPath().getString("name").length());
    }

    @Test
    void shouldReturnErrorForMalformedJson(){

        //Arrange
        String body = "{ \"name\": doggie }";

        //Act
        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .body(body)
                        .post("/pet");

        //Assert
        response.then()
                .statusCode(400)
                .body("$", hasKey("message"))
                .body("message", equalTo("bad input"));

    }

    @Test
    void shouldCreatePetWithoutName(){

        //Arrange
        String body = "{}";

        //Act
        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .body(body)
                        .post("/pet");

        //Assert
        response.then()
                .statusCode(200)
                .body("$", hasKey("id"))
                .body("$", not(hasKey("name")));
    }

    @Test
    void shouldUpdatePet(){

        //Arrange PUT
        Pet originalPet = new PetBuilder()
                .withName("initial")
                .build();

        //Act PUT
        Response createResponse = petClient.createPet(originalPet);
        long id = createResponse.jsonPath().getLong("id");

        Pet updatePet = new PetBuilder()
                .withId(id)
                .withName("lola")
                .build();

        Response updateResponse = petClient.updatePet(updatePet);

        //Assert PUT
        updateResponse.then()
                .statusCode(200)
                .body("name", equalTo("lola"));

        //Act GET
        Response getResponse = petClient.getPet(id);

        //Assert GET
        getResponse.then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("lola"));
    }

    @Test
    void shouldCreatePetWhenUpdatingNonExistingId(){

        //Arrange
        Pet originalPet = new PetBuilder()
                .withId(88888888888888L)
                .withName("doggie")
                .build();

        //Act
        given()
                .spec(RequestSpec.requestSpec)
                .body(originalPet)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo(88888888888888L))
                .body("name", equalTo("doggie"))
                .body("status", equalTo("available"))
                .log().body();

        given()
                .spec(RequestSpec.requestSpec)
                .when()
                .get("/pet/{id}", 88888888888888L)
                .then()
                .statusCode(200)
                .body("id", equalTo(88888888888888L))
                .body("name", equalTo("doggie"))
                .body("status", equalTo("available"));
    }
}
