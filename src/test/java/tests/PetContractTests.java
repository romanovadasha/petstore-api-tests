package tests;

import api.models.Pet;
import base.BaseTest;
import api.PetClient;
import api.models.PetBuilder;

import config.RequestSpec;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetContractTests extends BaseTest {

    private PetClient petClient = new PetClient();

    @Test
    void shouldKeepNullWhenNameIsExplicitlyNull(){

        Pet pet = new PetBuilder()
                .withName(null)
                .withStatus("available")
                .build();

        Pet createdPet = petClient.createPet(pet);

        assertThat(createdPet.getId(), greaterThan(0L));
        assertThat(createdPet.getName(), nullValue());
    }

    @Test
    void shouldSetDefaultNameWhenNameIsMissing(){

        Pet pet = new PetBuilder()
                .withStatus("available")
                .build();

        Pet createdPet = petClient.createPet(pet);

        assertThat(createdPet.getName(), nullValue());
    }

    @Test
    void shouldKeepEmptyName(){

        Pet pet = new PetBuilder()
                .withName("")
                .build();

        Pet createdPet = petClient.createPet(pet);

        assertThat(createdPet.getId(), greaterThan(0L));
        assertThat(createdPet.getName(), equalTo(""));
    }

    @Test
    void shouldKeepValidName(){

        Pet pet = new PetBuilder()
                .withName("doggie")
                .build();

        Pet createdPet = petClient.createPet(pet);

        assertThat(createdPet.getId(), greaterThan(0L));
        assertThat(createdPet.getName(), equalTo("doggie"));
        assertThat(createdPet.getStatus(), equalTo("available"));

    }

    @Test
    void shouldConvertNumericNameToString(){

        String body = "{ \"name\": 123 }";

        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .body(body)
                        .when()
                        .post("/pet");

        response.then()
                .statusCode(200)
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("name", equalTo("123"));

    }

    @Test
    void shouldConvertBooleanNameToString(){

        String body = "{ \"name\": true }";

        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .contentType("application/json")
                        .body(body)
                        .when()
                        .post("/pet");

        response.then()
                .statusCode(200)
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("name", equalTo("true"));
    }

    @Test
    void shouldKeepLongNameWithoutTruncation(){

        String longName = "a".repeat(1000);
        String body = "{ \"name\": \"" + longName + "\" }";

        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .contentType("application/json")
                        .body(body)
                        .post("/pet");

        //System.out.println(response.jsonPath().getString("name").length());

        response.then()
                .statusCode(200)
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("name", equalTo(longName));

    }

    @Test
    void shouldReturn400ForMalformedJson(){

        String body = "{ \"name\": doggie }";

        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .contentType("application/json")
                        .body(body)
                        .post("/pet");

        response.then()
                .statusCode(400)
                .body("$", not(hasKey("id")))
                .body("$", hasKey("message"))
                .body("message", equalTo("bad input")); //.body("message", containsString("input"))

    }

    @Test
    void shouldCreatePetWithoutName(){

        //Arrange
        String body = "{}";

        //Act
        Response response =
                given()
                        .spec(RequestSpec.requestSpec)
                        .contentType("application/json")
                        .body(body)
                        .post("/pet");

        //Assert
        response.then()
                .statusCode(200)
                .body("$", hasKey("id"))
                .body("$", not(hasKey("name")));

    }

    @Test
    void shouldPersistUpdatedNameAfterPut(){

        Pet originalPet = new PetBuilder()
                .withName("initial")
                .withStatus("available")
                .build();

        Pet createdPet = petClient.createPet(originalPet);
        long id = createdPet.getId();

        Pet updatedPet = new PetBuilder()
                .withId(id)
                .withName("lola")
                .build();

        petClient.updatePet(updatedPet);

        Pet fetchedPet = petClient.getPet(id);

        assertThat(fetchedPet.getId(), equalTo(id));
        assertThat(fetchedPet.getName(), equalTo("lola"));
        assertThat(fetchedPet.getStatus(), nullValue());

    }

    @Test
    void shouldCreatePetWhenUpdatingNonExistingId(){

        long id = 88888888888888L;

        Pet pet = new PetBuilder()
                .withId(id)
                .withName("doggie")
                .build();

        petClient.updatePet(pet);

        Pet fetchedPet = petClient.getPet(id);

        assertThat(fetchedPet.getId(), equalTo(id));
        assertThat(fetchedPet.getName(), equalTo("doggie"));
        assertThat(fetchedPet.getStatus(), equalTo("available"));
    }

    @Test
    void shouldMergeFieldsWhenUpdatingExistingPet(){

        long id = 7777777777L;

        Pet originalPet = new PetBuilder()
                .withId(id)
                .withName("doggie")
                .build();

        petClient.updatePet(originalPet);

        Pet updatedPet = new PetBuilder()
                .withId(id)
                .withName("lola")
                .build();

        petClient.updatePet(updatedPet);

        Pet fetchaedPet = petClient.getPet(id);

        assertThat(fetchaedPet.getId(), equalTo(id));
        assertThat(fetchaedPet.getName(), equalTo("lola"));
        assertThat(fetchaedPet.getStatus(), equalTo(originalPet.getStatus()));
    }

    @Test
    void shouldOverrideExistingFieldsWhenPuttingPartialPetData(){

        long id = 666666666L;

        Pet originalPet = new PetBuilder()
                .withId(id)
                .withName("doggie")
                .withStatus("sold")
                .withPhotoUrls(List.of("photo1"))
                .build();

        petClient.updatePet(originalPet);

        Pet updatePet = new PetBuilder()
                .withId(id)
                .withName("lola")
                .withStatus(null)
                .build();

        petClient.updatePet(updatePet);

        Pet fetchedPet = petClient.getPet(id);

        assertThat(fetchedPet.getId(), equalTo(id));
        assertThat(fetchedPet.getName(), equalTo("lola"));
        assertThat(fetchedPet.getStatus(), nullValue());

        // Reproduces JIRA-1234: partial PUT causes data loss
    }
}
