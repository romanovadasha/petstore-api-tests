package api.models;

import api.models.Tag;
import java.net.http.HttpResponse;
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

        //pet.id = this.id;

        if (id != null){
            pet.id = id;
        }

        //pet.name = this.name;

        if (isNameSet){
            pet.name = this.name;
        }

        if (areTagsSet) {
            pet.setTags(tags);
        }

        Tag t = new Tag(1L, "test");

        pet.status = this.status;

        if (arePhotoUrlsSet) {
            pet.photoUrls = this.photoUrls;
        }

        return pet;
    }

}