package api;

import api.models.Pet;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import config.RequestSpec;

public class PetClient {

    public Response createPetRaw(Pet pet) {
        return given()
                .spec(RequestSpec.requestSpec)
                .body(pet)
                .when()
                .post("/pet");
    }

    public Pet createPet(Pet pet){
        Response response = createPetRaw(pet);
        response.then().statusCode(200);
        Pet createPet = response.as(Pet.class);
        return createPet;

    }

    public Response getPetRaw(long petId) {
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .get("/pet/" + petId);
    }

    public Pet getPet(long petId){
        Response response = getPetRaw(petId);
        response.then().statusCode(200);
        Pet pet = response.as(Pet.class);
        return pet;
    }

    public Response deletePet(long petId) {
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .delete("/pet/" + petId);
    }

    public Response updatePet(Pet pet){
        return given()
                .spec(RequestSpec.requestSpec)
                .body(pet)
                .put("/pet");
    }

}
