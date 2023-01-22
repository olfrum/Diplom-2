package logic;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Client {
    public static String BASE_URI = "https://stellarburgers.nomoreparties.site/";

    protected static RequestSpecification getSpec() {
        return  new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}
