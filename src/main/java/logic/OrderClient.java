package logic;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String ENDPOINT_URI = "api/orders";

    @Step("Create order")
    public ValidatableResponse createOrder(String token, Order body){
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .body(body)
                .when()
                .post(ENDPOINT_URI)
                .then();
    }

    @Step("Get order")
    public ValidatableResponse getOrder(String token){
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .when()
                .get(ENDPOINT_URI)
                .then();
    }


}
