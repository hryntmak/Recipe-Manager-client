package cz.cvut.fit.tjv.recipe_client.service;

import cz.cvut.fit.tjv.recipe_client.api_client.IngredientClient;
import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class IngredientService {
    private IngredientClient ingredientClient;
    private long currentIngredient = 0;

    public IngredientService(IngredientClient ingredientClient) {
        this.ingredientClient = ingredientClient;
    }

    public boolean getCurrentIngredient() {
        return currentIngredient != 0;
    }

    public void setCurrentIngredient(long id) {
        this.currentIngredient = id;
        ingredientClient.setCurrentIngredient(id);
    }

    public Optional<IngredientDto> readOne() {
        var tmp = ingredientClient.readOne();
        return tmp;
    }

    public Collection<IngredientDto> readAll() {
        return ingredientClient.readAll();
    }

    public void create(IngredientDto data) {
        ingredientClient.create(data);
    }

    public void update(IngredientDto formData) {
        ingredientClient.update(formData);
    }

    public void deleteCurrent() {
        ingredientClient.deleteCurrent();
    }
}
