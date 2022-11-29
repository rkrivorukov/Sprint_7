package courier_creation;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import model.Courier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreationTest {

    private static final String LOGIN = "landsreyk";
    private static final String PASSWORD = "1234";
    private static final String FIRST_NAME = "saske";

    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        Response response = given().contentType("application/json")
                .body(Map.of("login", LOGIN, "password", PASSWORD))
                .post("/api/v1/courier/login");
        Map<String, String> map = response.getBody().as(new TypeRef<>() {
        });
        String id = map.get("id");
        given().contentType("application/json").delete("/api/v1/courier/" + id);
    }


    @Test
    @DisplayName("курьера можно создать")
    public void createCourierOnSuccess() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response response = given().contentType("application/json").body(courier).post("/api/v1/courier");
        response.prettyPrint();
        response.then().statusCode(201);
        Assert.assertEquals(response.getBody().asString(), "{\"ok\":true}");
    }

    @Test
    @DisplayName("нельзя создать двух одинаковых курьеров")
    public void createCourierOnError1() {
        Courier courier = new Courier("landsreyk", "1234", "saske");
        Response response = given().contentType("application/json").body(courier).post("/api/v1/courier");
        response.prettyPrint();
        response.then().statusCode(201);

        response = given().contentType("application/json").body(courier).post("/api/v1/courier");
        response.prettyPrint();
        response.then().statusCode(409);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля")
    public void createCourierOnError2() {
        String json = "{\"login\": \"landsreyk\"}";
        Response response = given().contentType("application/json").body(json).post("/api/v1/courier");
        response.prettyPrint();
        response.then().statusCode(400);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет, запрос возвращает ошибку")
    public void createCourierOnError3() {
        var json = "{\"password\": \"1234\"}";
        Response response = given().contentType("application/json").body(json).post("/api/v1/courier");
        response.prettyPrint();
        response.then().statusCode(400);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void createCourierOnError4() {
        createCourierOnSuccess();

        Courier courier = new Courier("landsreyk", "4321", "Andrew");
        Response response = given().contentType("application/json").body(courier).post("/api/v1/courier");
        response.prettyPrint();
        response.then().statusCode(409);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
