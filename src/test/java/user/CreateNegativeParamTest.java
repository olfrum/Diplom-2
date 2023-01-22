package user;

import generator.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import logic.UserClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.User;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreateNegativeParamTest {
    private final User user;
    private UserClient userClient;

    public CreateNegativeParamTest(User user) {
        this.user = user;
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
    }

    @Parameterized.Parameters
    public static Object[][] getUserWithEmptyFields(){
        User emptyMail = UserGenerator.getNewUser();
        emptyMail.setEmail(null);
        User emptyPassword = UserGenerator.getNewUser();
        emptyPassword.setPassword(null);
        User emptyName = UserGenerator.getNewUser();
        emptyName.setName(null);

        return  new Object[][]{
                {emptyMail},
                {emptyPassword},
                {emptyName},
        };
    }

    @Test
    @DisplayName("Create new user without mail/password/name")
    @Description("Result: status code 403")
    public void checkCreateWithoutRequiedFields(){
        ValidatableResponse validatableResponse = userClient.createUser(user);
        validatableResponse.assertThat().statusCode(403)
                .and().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"));
    }
}
