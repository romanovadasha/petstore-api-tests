package api.models;

import java.util.List;

public class PetBuilder {

    private Long id;
    private String name;
    private String status = "available";
    private List<String> photoUrls;
    private boolean arePhotoUrlsSet = false;
    private boolean isNameSet = false;
    private List<Tag> tags;
    private boolean areTagsSet = false;

    public static PetBuilder validPet() {
        return new PetBuilder()
                .withName("Boo")
                .withStatus("available");
    }

    public PetBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public PetBuilder withName(String name) {
        this.isNameSet = true;
        this.name = name;
        return this;
    }

    public PetBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public PetBuilder withPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
        this.arePhotoUrlsSet = true;
        return this;
    }

    public PetBuilder withTags(List<Tag> tags){
        this.tags = tags;
        this.areTagsSet = true;
        return this;
    }

    public PetBuilder withoutName(){
        this.isNameSet = false;
        this.name = null;
        return this;
    }

    public Pet build() {

        Pet pet = new Pet();

        if (id != null){
            pet.id = id;
        }

        if (isNameSet){
            pet.name = this.name;
        }

        if (areTagsSet) {
            pet.setTags(tags);
        }

        pet.status = this.status;

        if (arePhotoUrlsSet) {
            pet.photoUrls = this.photoUrls;
        }

        return pet;
    }

}