package user;

import generator.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import logic.OrderClient;
import logic.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateTest {
    User userData;
    UserClient userClient;
    ValidatableResponse validResponse;
    String authToken;
    String email;
    String pass;
    String name;

    @Before
    public void setUp(){
        userClient = new UserClient();
        userData = UserGenerator.getNewUser();
        validResponse = userClient.createUser(userData);

        authToken = validResponse.extract().path("accessToken");
        email = userData.getEmail();
        pass = userData.getPassword();
        name = userData.getName();

    }

    @Test
    @DisplayName("Change authorized user data")
    @Description("Result: status 200")
    public void changeAuthUserInfo(){
        userData.setEmail("a" + email);
        userData.setPassword("a" + pass);
        userData.setName("a" + name);

        ValidatableResponse validatableResponse = userClient.refreshInfo(authToken, userData);
        validatableResponse.assertThat().statusCode(200)
                .and().body("user.email", equalTo(userData.getEmail()))
                .and().body("user.name", equalTo(userData.getName()));

        ValidatableResponse validatableResponseAfterUpdate = userClient.getInfo(authToken);
        validatableResponseAfterUpdate.assertThat().statusCode(200)
                .and().body("user.email", equalTo(userData.getEmail()))
                .and().body("user.name", equalTo(userData.getName()));

        ValidatableResponse validatableResponseNewPassword = userClient.authUser(userData);
        validatableResponseNewPassword.assertThat().statusCode(200)
                .and().body("user.email", equalTo(userData.getEmail()))
                .and().body("user.name", equalTo(userData.getName()));
    }

    @Test
    @DisplayName("Change authorized user data")
    @Description("Result: status 200")
    public void changeUpdatedWithoutAuth(){
        userData.setEmail("a" + email);
        userData.setPassword("a" + pass);
        userData.setName("a" + name);

        ValidatableResponse validatableResponse = userClient.refreshInfo("", userData);
        validatableResponse.assertThat().statusCode(401)
                .and().body("message", equalTo("You should be authorised"));

        ValidatableResponse validatableResponseAfterUpdate = userClient.getInfo(authToken);
        validatableResponseAfterUpdate.assertThat().statusCode(200)
                .and().body("user.email", equalTo(email))
                .and().body("user.name", equalTo(name));

        ValidatableResponse validatableResponseNewPassword = userClient.authUser(userData);
        validatableResponseNewPassword.assertThat().statusCode(401)
                .and().body("message", equalTo("email or password are incorrect"));
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
