package courier_login;

import api.CourierApi;
import base.BaseRestAssuredTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import model.Courier;
import org.apache.http.HttpStatus;
import org.junit.*;

import java.util.Map;

public class CourierLoginTest extends BaseRestAssuredTest {
    private static final String LOGIN = "landsreyk";
    private static final String PASSWORD = "1234";
    private static final String FIRST_NAME = "saske";

    private static CourierApi courierApi;

    private Courier courier;

    @BeforeClass
    public static void beforeClass() {
        courierApi = new CourierApi();
    }

    @Before
    public void setUp() {
        courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        System.out.println("creating user");
        courierApi.create(courier);
    }

    @After
    public void tearDown() {
        courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        System.out.println("deleting user");
        courierApi.delete(courier);
    }

    @Test
    @DisplayName("курьер может авторизоваться")
    public void loginCourierOnSuccess() {
        Response response = courierApi.login(courier);
        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("система вернёт ошибку, если неправильно указать логин или пароль")
    public void loginCourierOnError1() {
        courier.setPassword("4321");
        Response response = courierApi.login(courier);
        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("если какого-то поля нет, запрос возвращает ошибку")
    public void loginCourierOnError2() {
        courier.setLogin(null);
        Response response = courierApi.login(courier);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        courier.setLogin(LOGIN);
        courier.setPassword(null);
        response = courierApi.login(courier);
        response.then().statusCode(HttpStatus.SC_GATEWAY_TIMEOUT);
    }

    @Test
    @DisplayName("если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void loginCourierOnError3() {
        courierApi.delete(courier);
        Response response = courierApi.login(courier);
        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("успешный запрос возвращает id")
    public void loginCourierOnSuccess2() {
        Response response = courierApi.login(courier);
        Map<String, String> map = response.getBody().as(new TypeRef<>() {
        });
        String id = map.get("id");
        Assert.assertTrue(id.matches("[0-9]+"));
    }


}
