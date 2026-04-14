package tests;

import api.models.Pet;
import base.BaseTest;
import api.PetClient;
import api.models.PetBuilder;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

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
}
