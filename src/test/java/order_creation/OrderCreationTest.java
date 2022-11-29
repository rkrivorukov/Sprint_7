package order_creation;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import model.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private Order order;

    public OrderCreationTest(Order order) {
        this.order = order;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("можно указать один из цветов — BLACK или GREY")
    public void createOrderOnSuccess() {
        Response response = given().contentType("application/json")
                .body(order)
                .post("/api/v1/orders");
        response.prettyPrint();
        response.then().statusCode(201);
    }

    @Test
    @DisplayName("можно указать оба цвета")
    public void createOrderOnSuccess2() {
        order.setColor(new String[]{"GREY", "BLACK"});
        Response response = given().contentType("application/json")
                .body(order)
                .post("/api/v1/orders");
        response.prettyPrint();
        response.then().statusCode(201);
    }

    @Test
    @DisplayName("можно совсем не указывать цвет")
    public void createOrderOnSuccess3() {
        order.setColor(null);
        Response response = given().contentType("application/json")
                .body(order)
                .post("/api/v1/orders");
        response.prettyPrint();
        response.then().statusCode(201);
    }

    @Test
    @DisplayName("тело ответа содержит track")
    public void createOrderOnSuccess4() {
        Response response = given().contentType("application/json")
                .body(order)
                .post("/api/v1/orders");
        response.prettyPrint();
        response.then().statusCode(201);
        String track = response.getBody().as(new TypeRef<Map<String, String>>() {
        }).get("track");
        Assert.assertTrue(track.matches("\\d+"));
    }

    @Parameters
    public static Collection<Object[]> createSampleOrder() {
        Order order = new Order();
        order.setFirstName("Naruto");
        order.setLastName("Uchiha");
        order.setAddress("Konoha, 142 apt.");
        order.setMetroStation("4");
        order.setPhone("+7 800 355 35 35");
        order.setRentTime(5);
        order.setDeliveryDate("2020-06-06");
        order.setComment("Saske, come back to Konoha");
        order.setColor(new String[]{"BLACK"});
        return Collections.singleton(new Object[]{order});
    }
}
