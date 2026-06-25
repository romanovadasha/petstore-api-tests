package api;

import api.models.Order;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import config.RequestSpec;

public class StoreClient {

    public Response getInventory() {
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .get("/store/inventory");
    }

    public Order createOrder(Order order) {
        Response response = createOrderRaw(order);
        response.then().statusCode(200);
        Order createOrder = response.as(Order.class);
        return createOrder;
    }

    public Response createOrderRaw(Order order) {
        return given()
                .spec(RequestSpec.requestSpec)
                .body(order)
                .when()
                .post("/store/order/");
    }

    public Response getRawOrder(long orderID) {
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .get("/store/order/" + orderID);
    }

    public Order getOrder(long orderID) {
        Response response = getRawOrder(orderID);
        response.then().statusCode(200);
        Order order = response.as(Order.class);
        return order;
    }

    public Response deleteOrderRaw(long orderID) {
        return given()
                .spec(RequestSpec.requestSpec)
                .when()
                .delete("/store/order/" + orderID);
    }
}
