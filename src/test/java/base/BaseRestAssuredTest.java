package base;

import io.restassured.RestAssured;

public class BaseRestAssuredTest {
    {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
}
