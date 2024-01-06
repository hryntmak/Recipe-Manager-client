package cz.cvut.fit.tjv.recipe_client.service;

import cz.cvut.fit.tjv.recipe_client.api_client.DishClient;
import cz.cvut.fit.tjv.recipe_client.model.DishDto;
import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import cz.cvut.fit.tjv.recipe_client.model.RecipeDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class DishService {
    private DishClient dishClient;
    private long currentDish = 0;

    public DishService(DishClient dishClient) {
        this.dishClient = dishClient;
    }

    public boolean getCurrentDish() {
        return currentDish != 0;
    }

    public void setCurrentDish(long id) {
        this.currentDish = id;
        dishClient.setCurrentDish(id);
    }

    public Optional<DishDto> readOne() {
        var tmp = dishClient.readOne();
        return tmp;
    }

    public Collection<DishDto> readAll() {
        return dishClient.readAll();
    }

    public void create(DishDto data) {
        dishClient.create(data);
    }

    public void update(DishDto formData) {
        dishClient.update(formData);
    }

    public void deleteCurrent() {
        dishClient.deleteCurrent();
    }

}
