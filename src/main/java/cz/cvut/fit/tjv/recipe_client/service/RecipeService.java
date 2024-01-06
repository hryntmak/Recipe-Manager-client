package cz.cvut.fit.tjv.recipe_client.service;

import cz.cvut.fit.tjv.recipe_client.api_client.RecipeClient;
import cz.cvut.fit.tjv.recipe_client.model.DishDto;
import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import cz.cvut.fit.tjv.recipe_client.model.RecipeDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class RecipeService {
    private RecipeClient recipeClient;
    private long currentRecipe = 0;

    public RecipeService(RecipeClient recipeClient) {
        this.recipeClient = recipeClient;
    }

    public boolean getCurrentRecipe() {
        return currentRecipe != 0;
    }

    public void setCurrentRecipe(long id) {
        this.currentRecipe = id;
        recipeClient.setCurrentRecipe(id);
    }

    public Optional<RecipeDto> readOne() {
        var tmp = recipeClient.readOne();
        return tmp;
    }

    public Collection<RecipeDto> readAll() {
        return recipeClient.readAll();
    }
    public Collection<IngredientDto> readRecipeIngredients(long id) {
        return recipeClient.readRecipeIngredients(id);
    }

    public void create(RecipeDto data) {
        recipeClient.create(data);
    }

    public void update(RecipeDto formData) {
        recipeClient.update(formData);
    }

    public void addIngredientToRecipe(IngredientDto formData) {
        RecipeDto recipe = recipeClient.readOne().get();
        int countOfIngredients = recipe.getContainIngredients().size() + 1;
        if (countOfIngredients > 3 && recipe.getComplexity().equals("LOW")) {
            recipe.setComplexity("MEDIUM");
            recipeClient.update(recipe);
        }
        if (countOfIngredients > 7 && recipe.getComplexity().equals("MEDIUM")) {
            recipe.setComplexity("HIGH");
            recipeClient.update(recipe);
        }
        recipeClient.addIngredient(formData);
    }

    public Collection<RecipeDto> readByPrice(double price) {
        return recipeClient.readByPrice(price);
    }

    public void deleteCurrent() {
        recipeClient.deleteCurrent();
    }
}
