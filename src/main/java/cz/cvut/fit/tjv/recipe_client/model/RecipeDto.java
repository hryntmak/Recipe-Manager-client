package cz.cvut.fit.tjv.recipe_client.model;

import java.util.Collection;

public class RecipeDto {
    private Long id;
    private String name;
    private String complexity;
    private int cookingTime;
    private DishDto dish;
    private Collection<IngredientDto> containIngredients;

    public Collection<IngredientDto> getContainIngredients() {
        return containIngredients;
    }

    public void setContainIngredients(Collection<IngredientDto> containIngredients) {
        this.containIngredients = containIngredients;
    }

    public DishDto getDish() {
        return dish;
    }

    public void setDish(DishDto dish) {
        this.dish = dish;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }
}
