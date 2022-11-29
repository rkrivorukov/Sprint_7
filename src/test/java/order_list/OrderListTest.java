package order_list;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("в тело ответа возвращается список заказов.")
    public void getCourierList() {
        Response response = given().contentType("application/json").get("/api/v1/orders");
        response.prettyPrint();
        response.then().statusCode(200);
    }

}
