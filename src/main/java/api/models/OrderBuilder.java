package api.models;

public class OrderBuilder {
    private Long id;
    private Long petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;

    public static OrderBuilder validOrder(){
        return new OrderBuilder()
                .withQuantity(1)
                .withStatus("placed");
    }

    public OrderBuilder withId(long id){
        this.id = id;
        return this;
    }

    public OrderBuilder withPetId(long petId){
        this.petId = petId;
        return this;
    }

    public OrderBuilder withQuantity(int quantity){
        this.quantity = quantity;
        return this;
    }

    public OrderBuilder withStatus(String status){
        this.status = status;
        return this;
    }

    public OrderBuilder withShipDate(String shipDate){
        this.shipDate = shipDate;
        return this;
    }

    public OrderBuilder withComplete(boolean complete){
        this.complete = complete;
        return this;
    }

    public Order build(){
        Order order = new Order();
        if (id != null) {
            order.id = id;
        }

        if (petId == null){
            throw new IllegalArgumentException("petId is required");
        }

        order.petId = this.petId;

        order.quantity = this.quantity;

        order.status = this.status;

        order.shipDate = this.shipDate;

        order.complete = this.complete;

        return order;
    }
}
