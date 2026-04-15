package service;

import api.PetClient;
import api.models.Pet;
import api.models.PetBuilder;
import config.RequestSpec;

import static io.restassured.RestAssured.given;

public class PetService {

    private PetClient petClient = new PetClient();

    public Pet createDefaultPet(){

        Pet pet = new PetBuilder()
                .withId(System.currentTimeMillis())
                .withName("Barsik")
                .withStatus("available")
                .build();
        petClient.createPet(pet);
        return pet;

    }

    public Pet createPet(Pet pet){
        return petClient.createPet(pet)
                .then()
                .extract()
                .as(Pet.class);
    }

    public Pet getPetById(long id){
        return petClient.getPet(id)
                .then()
                .statusCode(200)
                .extract()
                .as(Pet.class);
    }

    public void deletePet(Long id){
        petClient.deletePet(id)
                .then()
                .statusCode(200);
    }

}
