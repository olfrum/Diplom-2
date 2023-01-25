package order;

import generator.OrderGenerator;
import generator.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import logic.OrderClient;
import logic.UserClient;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojo.Order;
import pojo.User;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Order List")
public class GetUserOrdersTest {
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
    @DisplayName("Get Order list")
    @Description("Result: status code 200")
    public void getListOfOrderWithAuth(){
        Order order = new Order(OrderGenerator.getIngredients(1));
        orderClient.createOrder(authToken, order);

        ValidatableResponse validatableResponse = orderClient.getOrder(authToken);
        validatableResponse
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Get Order list without auth")
    @Description("Result: status code 200")
    public void getListOfOrderWithoutAuth(){
        Order order = new Order(OrderGenerator.getIngredients(1));
        orderClient.createOrder(authToken, order);

        ValidatableResponse validatableResponse = orderClient.getOrder("");
        validatableResponse
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
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
