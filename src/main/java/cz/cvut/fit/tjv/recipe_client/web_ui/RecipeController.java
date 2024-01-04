package cz.cvut.fit.tjv.recipe_client.web_ui;

import cz.cvut.fit.tjv.recipe_client.model.RecipeDto;
import cz.cvut.fit.tjv.recipe_client.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("/recipes")
public class RecipeController {
    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public String list(Model model) {
        var all = recipeService.readAll();
        model.addAttribute("allRecipes", all);
        return "recipes";
    }

    @GetMapping("/edit")
    public String showForm(Model model, @RequestParam long id) {
        recipeService.setCurrentRecipe(id);
        var currRecipe = recipeService.readOne().get();
        model.addAttribute("recipe", currRecipe);
        return "editRecipe";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute RecipeDto formData, Model model) {
        recipeService.setCurrentRecipe(formData.getId());
        try {
            recipeService.update(formData);
        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", true);
        }
        return list(model);
    }

}
