package tests;

import api.models.PetBuilder;
import base.BaseTest;

import api.PetClient;
import api.models.Pet;

import static api.models.PetBuilder.validPet;
import static org.hamcrest.Matchers.*;

import config.ResponseSpec;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.PetService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class PetTests extends BaseTest {

    private List<Long> createdPetIds = new ArrayList<>();

    private PetService petService = new PetService();

    private PetClient petClient = new PetClient();

    @Test
    void shouldAcceptInvalidStatus(){

        // Arrange
        Pet pet = new PetBuilder()
                .withId(System.currentTimeMillis())
                .withStatus("INVALID")
                .withName("Boo")
                .build();

        // Act
        Pet created = petService.createPet(pet);
        createdPetIds.add(created.id);

        // Assert
        assertEquals("INVALID", created.status);
        assertEquals("Boo", created.name);
        assertNotNull(created.id);

    } // Тут есть баг

    @Test
    void shouldCreatePetWithValidData(){

        // Arrange
        Pet pet = validPet().build();

        // Act
        Pet created = petService.createPet(pet);
        createdPetIds.add(created.id);

        // + Act
        Pet fetched = petService.getPetById(created.id);

        // Asserts
        assertNotNull(created.id);

        assertEquals(pet.name, created.name);
        assertEquals(pet.status, created.status);

        assertEquals(pet.name, fetched.name);
        assertEquals(pet.status, fetched.status);

    }

    @Test
    void shouldDeletePetAndMakeItUnavailable() {

        //Arrange
        Pet pet = validPet().build();

        //Act
        Pet created = petService.createPet(pet);
        long id = created.id;

        createdPetIds.add(id);

        Response deleteResponse = petClient.deletePet(id);

        Response deletedPetResponse = petClient.getPetRaw(id);

        String message = deletedPetResponse.jsonPath().getString("message");

        if(deleteResponse.statusCode() == 200){
            createdPetIds.remove(Long.valueOf(id));
        }

        //Asserts
        assertEquals(200 ,deleteResponse.statusCode());
        assertEquals(404, deletedPetResponse.statusCode());
        assertEquals("Pet not found", message);

    }

    @Test
    void shouldReturnNotFoundUponRequestNonExistentPetId(){

        //Arrange
        long nonExistingId = 9999999999L;

        //Act
        Response getResponse = petClient.getPetRaw(nonExistingId);
        String message = getResponse.jsonPath().getString("message");

        //Asserts
        assertEquals(404, getResponse.statusCode());
        assertEquals("Pet not found", message);
    }

    @Test
    void shouldUpdatePetAndReturnModifiedPet(){

        long id = System.currentTimeMillis();

        //Arrange
        Pet initialPet = new PetBuilder()
                .withId(id)
                .withName("bobby")
                .withStatus("available")
                .build();

        Pet created = petService.createPet(initialPet);
        createdPetIds.add(created.id);

        //Act
        Pet updatePet = new PetBuilder()
                .withId(id)
                .withName("popy")
                .withStatus("sold")
                .build();

        Response updateResponse = petClient.updatePet(updatePet);

        Pet fetched = petService.getPetById(id);

        //Asserts
        assertEquals(200, updateResponse.statusCode());
        assertEquals("popy", fetched.getName());
        assertEquals("sold", fetched.getStatus());
        assertEquals(id, fetched.getId());

    }

    @AfterEach
    void cleanup() {
        for (Long id : createdPetIds) {
            petService.deletePet(id);
        }
        createdPetIds.clear();
    }
}
