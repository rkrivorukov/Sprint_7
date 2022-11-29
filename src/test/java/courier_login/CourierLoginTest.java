package courier_login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CourierLoginTest {
    private static final String LOGIN = "landsreyk";
    private static final String PASSWORD = "1234";
    private static final String FIRST_NAME = "saske";

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        System.out.println("creating user");
        given().contentType("application/json").body(courier).post("/api/v1/courier");
    }

    @After
    public void tearDown() {
        Response response = given().contentType("application/json")
                .body(Map.of("login", LOGIN, "password", PASSWORD))
                .post("/api/v1/courier/login");
        Map<String, String> map = response.getBody().as(new TypeRef<>() {
        });
        String id = map.get("id");
        System.out.println("deleting user");
        given().contentType("application/json").delete("/api/v1/courier/" + id);
    }

    @Test
    @DisplayName("курьер может авторизоваться")
    public void loginCourierOnSuccess() {
        Response response = given().contentType("application/json")
                .body(Map.of("login", LOGIN, "password", PASSWORD))
                .post("/api/v1/courier/login");
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test
    @DisplayName("система вернёт ошибку, если неправильно указать логин или пароль")
    public void loginCourierOnError1() {
        Response response = given().contentType("application/json")
                .body(Map.of("login", LOGIN, "password", 4321))
                .post("/api/v1/courier/login");
        response.then().statusCode(404);
        response.prettyPrint();
    }

    @Test
    @DisplayName("если какого-то поля нет, запрос возвращает ошибку")
    public void loginCourierOnError2() {
        Response response = given().contentType("application/json")
                .body(Map.of("password", PASSWORD))
                .post("/api/v1/courier/login");
        response.then().statusCode(400);
        response.prettyPrint();

        response = given().contentType("application/json")
                .config(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout",10000)))
                .body(Map.of("login", LOGIN))
                .post("/api/v1/courier/login");
        response.then().statusCode(504);
        response.prettyPrint();
    }

    @Test
    @DisplayName("если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void loginCourierOnError3() {
        tearDown();
        Response response = given().contentType("application/json")
                .body(Map.of("login", LOGIN, "password", PASSWORD))
                .post("/api/v1/courier/login");
        response.then().statusCode(404);
        response.prettyPrint();
    }

    @Test
    @DisplayName("успешный запрос возвращает id")
    public void loginCourierOnSuccess2() {
        Response response = given().contentType("application/json")
                .body(Map.of("login", LOGIN, "password", PASSWORD))
                .post("/api/v1/courier/login");
        response.prettyPrint();
        Map<String, String> map = response.getBody().as(new TypeRef<>() {
        });
        String id = map.get("id");
        Assert.assertTrue(id.matches("[0-9]+"));
    }


}
