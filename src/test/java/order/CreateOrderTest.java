package order;

import generator.OrderGenerator;
import generator.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import logic.OrderClient;
import logic.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {

    User userData;
    UserClient userClient;
    ValidatableResponse validResponse;
    OrderClient orderClient;
    String authToken;

    @Before
    public void setUp(){
        userData = UserGenerator.getNewUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        validResponse = userClient.createUser(userData);
        authToken = validResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Created order with empty ingredient")
    @Description("Result : status code 500")
    public void createdWithEmptyIngredient(){
        List<String> ingredient = OrderGenerator.getIngredients(1);
        ingredient.set(0, null);
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderClient.createOrder(authToken, order);
        validatableResponse
                .assertThat().statusCode(500);
    }

    @Test
    @DisplayName("Created order with wrong ingredient")
    @Description("Result : status code 400")
    public void createdWithWrongIngredient(){
        List<String> ingredient = OrderGenerator.getIngredients(1);
        ingredient.set(0, "q" + ingredient.get(0));
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderClient.createOrder(authToken, order);
        validatableResponse
                .assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Created order with without auth")
    @Description("Result : status code 200")
    public void createdOrderWithoutAuth(){
        List<String> ingredient = OrderGenerator.getIngredients(1);
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderClient.createOrder("", order);
        validatableResponse
                .assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Created order with with auth")
    @Description("Result : status code 200")
    public void createdOrderWithAuth(){
        List<String> ingredient = OrderGenerator.getIngredients(1);
        Order order = new Order(ingredient);
        ValidatableResponse validatableResponse = orderClient.createOrder(authToken, order);
        validatableResponse
                .assertThat().statusCode(200)
                .and()
                .body("order.status", equalTo("done"));
    }

    @After
    public void cleanUp() {
        if (authToken != null){
            userClient
                    .deleteUser(authToken)
                    .assertThat()
                    .statusCode(202);
        }
    }
}
