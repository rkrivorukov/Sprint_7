package api;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import model.Courier;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierApi {

    public Response create(Courier courier) {
        Response response = given().contentType("application/json")
                .body(courier)
                .post("/api/v1/courier");
        response.prettyPrint();
        return response;
    }

    public Response login(Courier courier) {
        Response response = given().contentType("application/json")
                .body(courier)
                .post("/api/v1/courier/login");
        response.prettyPrint();
        return response;
    }

    public Response delete(Courier courier) {
        Response response = login(courier);
        Map<String, String> map = response.getBody().as(new TypeRef<>() {
        });
        String id = map.get("id");
        response = given().contentType("application/json").delete("/api/v1/courier/" + id);
        response.prettyPrint();
        return response;
    }
}
