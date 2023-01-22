package logic;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pojo.Credentials;
import pojo.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client{
    private  final String REGISTER_ENDPOINT_URI = "api/auth/register";
    private  final String LOGIN_ENDPOINT_URI = "api/auth/login";
    private  final String AUTH_ENDPOINT_URI = "api/auth/user";

    @Step("Create user")
    public ValidatableResponse createUser(User body){
        return given()
                .spec(getSpec())
                .body(body)
                .when()
                .post(REGISTER_ENDPOINT_URI)
                .then();
    }
    @Step("Auth user")
    public  ValidatableResponse authUser(User body){
        return given()
                .spec(getSpec())
                .body(body)
                .when()
                .post(LOGIN_ENDPOINT_URI)
                .then();
    }
    @Step("Refresh info")
    public  ValidatableResponse refreshInfo(String token, User body){
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .body(body)
                .when()
                .patch(AUTH_ENDPOINT_URI)
                .then();
    }
    @Step("Get info")
    public  ValidatableResponse getInfo (String token){
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .when()
                .get(AUTH_ENDPOINT_URI)
                .then();
    }

    @Step("Delete user")
    public  ValidatableResponse deleteUser (String token){
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .when()
                .delete(AUTH_ENDPOINT_URI)
                .then();
    }

}
