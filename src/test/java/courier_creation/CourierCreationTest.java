package courier_creation;

import api.CourierApi;
import base.BaseRestAssuredTest;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import org.apache.http.HttpStatus;
import org.junit.*;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreationTest extends BaseRestAssuredTest {

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
    }

    @After
    public void tearDown() {
        courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        System.out.println("deleting user");
        courierApi.delete(courier);
    }

    @Test
    @DisplayName("курьера можно создать")
    public void createCourierOnSuccess() {
        Response response = courierApi.create(courier);
        response.then().statusCode(HttpStatus.SC_CREATED);
        Assert.assertEquals(response.getBody().asString(), "{\"ok\":true}");
    }

    @Test
    @DisplayName("нельзя создать двух одинаковых курьеров")
    public void createCourierOnError1() {
        Response response = courierApi.create(courier);
        response.then().statusCode(HttpStatus.SC_CREATED);

        response = courierApi.create(courier);
        response.then().statusCode(HttpStatus.SC_CONFLICT);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля")
    public void createCourierOnError2() {
        courier.setPassword(null);
        courier.setFirstName(null);
        Response response = courierApi.create(courier);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет, запрос возвращает ошибку")
    public void createCourierOnError3() {
        courier.setLogin(null);
        courier.setFirstName(null);
        Response response = courierApi.create(courier);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void createCourierOnError4() {
        courierApi.create(courier);

        Courier courier2 = new Courier("landsreyk", "4321", "Andrew");
        Response response = courierApi.create(courier2);
        response.then().statusCode(HttpStatus.SC_CONFLICT);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
