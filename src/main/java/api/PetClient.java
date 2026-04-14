package api;

import api.models.Pet;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import config.RequestSpec;
import io.restassured.http.ContentType;
import io.restassured.response.Validatable;

public class PetClient {

    public Response createPet(Pet pet) {
        return given()
                .spec(RequestSpec.requestSpec)
                .body(pet)
                .when()
                .post("/pet");
    }

    public Response getPet(long petId) {
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .get("/pet/" + petId);
    }

    public Response deletePet(long petId) {
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .delete("/pet/" + petId);
    }

}
