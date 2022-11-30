package order_list;

import base.BaseRestAssuredTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrderListTest extends BaseRestAssuredTest {

    @Test
    @DisplayName("в тело ответа возвращается список заказов.")
    public void getCourierList() {
        Response response = given().contentType("application/json").get("/api/v1/orders");
        response.prettyPrint();
        response.then().statusCode(HttpStatus.SC_OK);
    }

}
