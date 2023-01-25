package user;

import generator.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import logic.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.hamcrest.core.IsEqual.equalTo;

    @DisplayName("Create User")
    public class CreateTest {

    User userData;
    ValidatableResponse validResponse;
    UserClient userClient;

    @Before
    public void setUp(){
        userClient = new UserClient();
        userData = UserGenerator.getNewUser();
        validResponse = userClient.createUser(userData);
    }

    @Test
    @DisplayName("Create new user with correct data")
    @Description("Result : status code 200")
    public void createCourierWithCorrectValueData(){
        validResponse
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create of copy user")
    @Description("Result: status code 403")
    public void createTwoEqualUsers() {
        validResponse
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));

        ValidatableResponse errorResponse = userClient.createUser(userData);

        errorResponse
                .statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
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
