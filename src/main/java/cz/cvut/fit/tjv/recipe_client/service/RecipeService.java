package cz.cvut.fit.tjv.recipe_client.service;

import cz.cvut.fit.tjv.recipe_client.api_client.RecipeClient;
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
        recipeClient.addIngredient(formData);
        // TODO
    }

    public Collection<RecipeDto> readByPrice(double price) {
        return recipeClient.readByPrice(price);
    }

    public void deleteCurrent() {
        recipeClient.deleteCurrent();
    }

//    public long createPostForCurrentUser(PostDto data) {
//        var myPosts = postClient.readByAuthor(currentUser); //1st request
//
//        if (myPosts.size() > 3) //business logic decision
//            throw new FreePostLimitExceededException();
//
//        data.setAuthor(userClient.readOne().get());
//
//        userClient.createMyPost(data); //2nd request
//    }

}
