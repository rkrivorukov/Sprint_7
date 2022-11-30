package service;

import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderApi {

    public Response create(Order order) {
        Response response = given().contentType("application/json")
                .body(order)
                .post("/api/v1/orders");
        response.prettyPrint();
        return response;
    }
}
