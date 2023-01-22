package user;

import com.github.javafaker.Faker;
import generator.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import logic.OrderClient;
import logic.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Credentials;
import pojo.User;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class LoginTest {
    User userData;
    UserClient userClient;
    ValidatableResponse validResponse;
    OrderClient orderClient;

    @Before
    public void setUp(){
        userClient = new UserClient();
        userData = UserGenerator.getNewUser();
        validResponse = userClient.createUser(userData);
    }

    @Test
    @DisplayName("Auth correct user")
    @Description("Result : status code 200")
    public void loginExistUserTest(){
        ValidatableResponse validatableResponse = userClient.authUser(userData);
        validatableResponse.assertThat().statusCode(200)
                .and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Wrong email")
    @Description("Result : status code 401")
    public void loginNonExistEmail(){
        String validEmail = userData.getEmail();
        userData.setEmail("1" + userData.getEmail());
        ValidatableResponse validatableResponse = userClient.authUser(userData);
        validatableResponse
                .statusCode(401)
                .and().body("message", equalTo("email or password are incorrect"));
        userData.setEmail(validEmail);
    }
    @Test
    @DisplayName("Wrong email")
    @Description("Result : status code 401")
    public void loginNonExistPassword(){
        String validPass = userData.getPassword();
        userData.setEmail("1" + userData.getEmail());
        ValidatableResponse validatableResponse = userClient.authUser(userData);
        validatableResponse
                .statusCode(401)
                .and().body("message", equalTo("email or password are incorrect"));
        userData.setEmail(validPass);
    }

    @After
    public void cleanUp() {
        String authToken = validResponse.extract().path("accessToken");
        if (authToken != null){
            userClient
                    .deleteUser(authToken)
                    .assertThat()
                    .statusCode(202);
        }
    }
}
