package logic;

import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends Client{
    private final String INGREDIENTS_URI = "api/ingredients";
    private final Filter reqFilt = new RequestLoggingFilter();
    private final Filter respFilt = new ResponseLoggingFilter();

    @Step("Get ingredients")
    public ValidatableResponse getIngredients(){
        return given()
                .filters(reqFilt, respFilt)
                .spec(getSpec())
                .when()
                .get(INGREDIENTS_URI)
                .then();
    }
}
