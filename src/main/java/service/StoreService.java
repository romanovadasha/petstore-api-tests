package service;

import api.StoreClient;
import api.models.Order;
import api.models.Pet;
import io.restassured.response.Response;

import static api.models.OrderBuilder.validOrder;

public class StoreService {
    private StoreClient storeClient = new StoreClient();
    private PetService petService = new PetService();

    public Order createDefaultOrder(){

        Pet pet = petService.createDefaultPet();
        long petId = pet.getId();

        Order order = validOrder().withPetId(petId).build();

        return storeClient.createOrder(order);
    }

    public Order createOrder(Order order){
        return storeClient.createOrder(order);
    }

    public Order getOrder(long id){
        return storeClient.getOrder(id);
    }

    public Response getOrderRaw(long id){
        return storeClient.getRawOrder(id);
    }

    public Response deleteOrder(long orderId){
        return storeClient.deleteOrderRaw(orderId);
    }

    public Response getInventory(){
        return storeClient.getInventory();
    }
}
