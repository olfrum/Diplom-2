package generator;

import logic.IngredientsClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {

    @Step("Get new ingredients")
    public static List<String> getIngredients(int countOfIngredients) {
        IngredientsClient ingredientsClient = new IngredientsClient();
        ValidatableResponse response = ingredientsClient.getIngredients();
        List<String> ingredientsIds = new ArrayList<>();
        if (countOfIngredients > 0) {
            for (int i = 0; i < countOfIngredients; i++) {
                String ingredientId = response.extract().body().path(String.format("data[%d]._id", i));
                ingredientsIds.add(ingredientId);
            }
        }
        return ingredientsIds;
    }
}
