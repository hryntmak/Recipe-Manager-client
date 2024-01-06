package cz.cvut.fit.tjv.recipe_client.web_ui;

import cz.cvut.fit.tjv.recipe_client.model.DishDto;
import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import cz.cvut.fit.tjv.recipe_client.model.RecipeDto;
import cz.cvut.fit.tjv.recipe_client.service.DishService;
import cz.cvut.fit.tjv.recipe_client.service.IngredientService;
import cz.cvut.fit.tjv.recipe_client.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Controller
@RequestMapping("/dishes")
public class DishController {
    private DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public String list(Model model) {
        var all = dishService.readAll();
        model.addAttribute("allDishes", all);
        model.addAttribute("dish", new DishDto());
        return "dishes";
    }


    @PostMapping
    public String delete(Model model, @RequestParam long id) {
        dishService.setCurrentDish(id);
        dishService.deleteCurrent();
        model.addAttribute("allDishes", dishService.readAll());
        model.addAttribute("dish", new DishDto());
        return "dishes";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute DishDto formData, Model model) {
        dishService.create(formData);
        return list(model);
    }

    @GetMapping("/edit")
    public String showForm(Model model, @RequestParam long id) {
        dishService.setCurrentDish(id);
        var currDish = dishService.readOne().get();
        model.addAttribute("dish", currDish);
        return "editDish";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute DishDto formData, Model model) {
        dishService.setCurrentDish(formData.getId());
        try {
            dishService.update(formData);
        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", true);
        }
        return list(model);
    }
}