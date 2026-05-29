package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    public long id;
    public  long petId;
    public int quantity;
    public String shipDate;
    public String status;
    public boolean complete;

    public long getId(){
        return id;
    }

    public long getPetId(){
        return petId;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getShipDate(){
        return shipDate;
    }

    public  String getStatus(){
        return status;
    }

    public  boolean isComplete(){
        return complete;
    }
}
