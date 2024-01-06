package cz.cvut.fit.tjv.recipe_client.web_ui;

import cz.cvut.fit.tjv.recipe_client.model.DishDto;
import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import cz.cvut.fit.tjv.recipe_client.service.IngredientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {
    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String list(Model model) {
        var all = ingredientService.readAll();
        model.addAttribute("allIngredients", all);
        model.addAttribute("ingredient", new IngredientDto());
        return "ingredients";
    }


    @PostMapping
    public String delete(Model model, @RequestParam long id) {
        ingredientService.setCurrentIngredient(id);
        ingredientService.deleteCurrent();
        model.addAttribute("allIngredients", ingredientService.readAll());
        model.addAttribute("ingredient", new IngredientDto());
        return "ingredients";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute IngredientDto formData, Model model) {
        ingredientService.create(formData);
        return list(model);
    }

    @GetMapping("/edit")
    public String showForm(Model model, @RequestParam long id) {
        ingredientService.setCurrentIngredient(id);
        var currDish = ingredientService.readOne().get();
        model.addAttribute("ingredient", currDish);
        return "editIngredient";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute IngredientDto formData, Model model) {
        ingredientService.setCurrentIngredient(formData.getId());
        try {
            ingredientService.update(formData);
        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", true);
        }
        return list(model);
    }
}