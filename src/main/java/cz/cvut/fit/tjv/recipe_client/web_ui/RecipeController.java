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
@RequestMapping("/recipes")
public class RecipeController {
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private DishService dishService;

    public RecipeController(RecipeService recipeService, IngredientService ingredientService, DishService dishService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.dishService = dishService;
    }

    @GetMapping
    public String list(Model model, @RequestParam Optional<Double> price) {
        if (price.isPresent()) {
            var byPrice = recipeService.readByPrice(price.get());
            model.addAttribute("allRecipes", byPrice);
            IngredientDto recipePrice = new IngredientDto();
            recipePrice.setId(0L);
            recipePrice.setName("Price");
            recipePrice.setPrice(price.get());
            model.addAttribute("recipePrice", recipePrice);
        } else {
            var all = recipeService.readAll();
            model.addAttribute("allRecipes", all);
            IngredientDto recipePrice = new IngredientDto();
            recipePrice.setId(0L);
            recipePrice.setName("Price");
            recipePrice.setPrice(0);
            model.addAttribute("recipePrice", recipePrice);
        }
        model.addAttribute("recipe", new RecipeDto());
        return "recipes";
    }


    @PostMapping
    public String delete(Model model, @RequestParam long id) {
        recipeService.setCurrentRecipe(id);
        recipeService.deleteCurrent();
        model.addAttribute("allRecipes", recipeService.readAll());
        IngredientDto recipePrice = new IngredientDto();
        recipePrice.setId(0L);
        recipePrice.setName("Price");
        recipePrice.setPrice(0);
        model.addAttribute("recipePrice", recipePrice);
        model.addAttribute("recipe", new RecipeDto());
        return "recipes";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute RecipeDto formData, Model model) {
        recipeService.create(formData);
        return list(model, Optional.empty());
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
        return list(model, Optional.empty());
    }

    @GetMapping("/ingredients")
    public String showRecipeIngredients(Model model, @RequestParam long id) {
        recipeService.setCurrentRecipe(id);
        var currIngredients = recipeService.readRecipeIngredients(id);
        model.addAttribute("allIngredients", currIngredients);
        var currRecipe = recipeService.readOne().get();
        model.addAttribute("recipe", currRecipe);
        return "recipeIngredients";
    }

    @PostMapping("/ingredients")
    public String deleteIngredient(Model model, @RequestParam long id) {
        recipeService.deleteIngredientFromCurrent(id);
        var currIngredients = recipeService.readRecipeIngredients(id);
        model.addAttribute("allIngredients", currIngredients);
        var currRecipe = recipeService.readOne().get();
        model.addAttribute("recipe", currRecipe);
        return "recipeIngredients";
    }

    @GetMapping("/newIngredients")
    public String showAddIngredients(Model model, @RequestParam long id) {
        recipeService.setCurrentRecipe(id);
        var allIngredients = ingredientService.readAll();
        model.addAttribute("allIngredients", allIngredients);
        var currRecipe = recipeService.readOne().get();
        model.addAttribute("recipe", currRecipe);
        return "addIngredientsToRecipe";
    }

    @PostMapping("/newIngredients")
    public String addIngredientsSubmit(@ModelAttribute IngredientDto formData, Model model, @RequestParam long id) {
        try {
            formData.setId(id);
            recipeService.addIngredientToRecipe(formData);
            var allIngredients = ingredientService.readAll();
            model.addAttribute("allIngredients", allIngredients);
            var currRecipe = recipeService.readOne().get();
            model.addAttribute("recipe", currRecipe);
        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", true);
        }
        return "addIngredientsToRecipe";
    }

    @GetMapping("/changeDish")
    public String changeDish(Model model, @RequestParam long id) {
        recipeService.setCurrentRecipe(id);
        var allDishes = dishService.readAll();
        model.addAttribute("allDishes", allDishes);
        return "changeDish";
    }

    @PostMapping("/changeDish")
    public String submitChangeDish(@ModelAttribute DishDto formData, Model model, @RequestParam long id) {
        try {
            formData.setId(id);
            RecipeDto recipe = recipeService.readOne().get();
            recipe.setDish(formData);
            recipeService.update(recipe);
        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", true);
        }
        return list(model, Optional.empty());
    }
}
