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


    @Test
    void shouldCreatePet() {

        // Arrange
        Pet pet = petService.createDefaultPet();
        createdPetIds.add(pet.id);

        // Act
        Pet actualPet = petService.getPetById(pet.id);

        // Assert
        assertEquals(pet.name, actualPet.name);
        assertEquals(pet.status, actualPet.status);

    }

    @Test
    void shouldReturnPetById(){

        // Arrange
        Pet pet = petService.createDefaultPet();
        createdPetIds.add(pet.id);

        // Act
        Pet actualPet = petService.getPetById(pet.id);

        // Assert
        assertEquals(pet.id, actualPet.id);
        assertEquals(pet.name, actualPet.name);

    }

    @Test
    void shouldIgnoreNullName(){

        // Arrange
        Pet pet = new PetBuilder()
                .withId(System.currentTimeMillis())
                .withName(null)
                .withStatus("available")
                .build();

        // Act
        Pet created = petService.createPet(pet);
        createdPetIds.add(created.id);

        // Assert
        assertNull(created.name);
        assertNotNull(created.id);
        assertEquals("available", created.status);
    }

    @Test
    void shouldSetDefaultNameWhenMissing(){

        // Arrange
        Pet pet = new PetBuilder()
                .withId(System.currentTimeMillis())
                .withStatus("available")
                .build();

        // Act
        Pet created = petService.createPet(pet);
        createdPetIds.add(created.id);

        // Assert
        assertNotNull(created.id);
        assertNotNull(created.name);
        assertEquals("available", created.status);

        // API sets default name when field is missing (unexpected behavior)
    }

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

        // + Act
        Pet fetched = petService.getPetById(created.id);

        // Asserts
        assertNotNull(created.id);

        assertEquals(pet.name, created.name);
        assertEquals(pet.status, created.status);

        assertEquals(pet.name, fetched.name);
        assertEquals(pet.status, fetched.status);

    }

    @AfterEach
    void cleanup() {
        for (Long id : createdPetIds) {
            petService.deletePet(id);
        }
        createdPetIds.clear();
    }
}
