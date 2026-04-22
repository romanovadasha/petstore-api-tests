package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {

    public long id;

    public long getId(){
        return id;
    }

    public String name;

    public String status;

    public List<String> photoUrls;

    public String getName(){
        return name;
    }

    public String getStatus(){
        return status;
    }

}
