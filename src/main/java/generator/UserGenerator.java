package generator;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import pojo.User;

import java.util.Locale;

public class UserGenerator {
    @Step("Generate new user")
    public static User getNewUser(){
        Faker faker = new Faker(new Locale("en"));
        return new User (
                faker.internet().emailAddress(),
                faker.internet().password(8, 10),
                faker.name().firstName());
    }
}
