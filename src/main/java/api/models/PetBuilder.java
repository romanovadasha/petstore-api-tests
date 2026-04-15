package api.models;

public class PetBuilder {

    private long id = System.currentTimeMillis();
    private String name = "Barsik";
    private String status = "available";

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

    public Pet build() {
        Pet pet = new Pet();
        pet.id = this.id;
        pet.name = this.name;
        pet.status = this.status;
        return pet;
    }
}