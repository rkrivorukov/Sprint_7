package order_creation;

import api.OrderApi;
import base.BaseRestAssuredTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import model.Order;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

public class OrderCreationTest extends BaseRestAssuredTest {

    private static OrderApi orderApi;

    private Order order;

    @BeforeClass
    public static void beforeClass() {
        orderApi = new OrderApi();
    }

    @Before
    public void setUp() {
        order = createSampleOrder();
    }

    @Test
    @DisplayName("можно указать один из цветов — BLACK или GREY")
    public void createOrderOnSuccess() {
        Response response = orderApi.create(order);
        response.then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("можно указать оба цвета")
    public void createOrderOnSuccess2() {
        order.setColor(new String[]{"GREY", "BLACK"});
        Response response = orderApi.create(order);
        response.then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("можно совсем не указывать цвет")
    public void createOrderOnSuccess3() {
        order.setColor(null);
        Response response = orderApi.create(order);
        response.then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("тело ответа содержит track")
    public void createOrderOnSuccess4() {
        Response response = orderApi.create(order);
        response.then().statusCode(HttpStatus.SC_CREATED);
        String track = response.getBody().as(new TypeRef<Map<String, String>>() {
        }).get("track");
        Assert.assertTrue(track.matches("\\d+"));
    }

    public Order createSampleOrder() {
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
        return order;
    }
}
