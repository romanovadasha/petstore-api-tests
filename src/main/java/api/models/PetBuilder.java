package api.models;

import java.net.http.HttpResponse;
import java.util.List;

public class PetBuilder {

    private long id = System.currentTimeMillis();
    private String name = "Barsik";
    private String status = "available";
    private List<String> photoUrls;

    public static Pet validPet() {
        return new PetBuilder()
                .withName("Boo")
                .withStatus("available")
                .build();
    }

    public PetBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public PetBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PetBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public PetBuilder withPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
        return this;
    }

    public PetBuilder withoutName(){
        this.name = null;
        return this;
    }

    public Pet build() {
        Pet pet = new Pet();
        pet.id = this.id;
        //pet.name = this.name;
        if (this.name != null) {
            pet.name = this.name;
        }
        pet.status = this.status;
        pet.photoUrls = this.photoUrls;
        return pet;
    }


}