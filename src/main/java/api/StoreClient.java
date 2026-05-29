package api;

import api.models.Order;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import config.RequestSpec;

public class StoreClient {

    public Response getInventory(){
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .get("/store/inventory");
    }

    public Order createOrder(Order order){
        Response response = createOrderRaw(order);
        response.then().statusCode(200);
        Order createOrder = response.as(Order.class);
        return createOrder;
    }

    public Response createOrderRaw(Order order){
        return given()
                .spec(RequestSpec.requestSpec)
                .body(order)
                .when()
                .post("/store/order/");
    }
}
