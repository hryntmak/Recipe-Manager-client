package cz.cvut.fit.tjv.recipe_client.api_client;

import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import cz.cvut.fit.tjv.recipe_client.model.RecipeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class RecipeClient {
    private String baseUrl;
    private RestClient recipeRestClient;
    private RestClient currentRecipeRestClient;

    public RecipeClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        recipeRestClient = RestClient.create(baseUrl + "/recipes");
    }

    public void setCurrentRecipe(long id) {
        currentRecipeRestClient = RestClient.builder()
                .baseUrl(baseUrl + "/recipes/{idRecipe}")
                .defaultUriVariables(Map.of("idRecipe", id))
                .build();
    }

    public Optional<RecipeDto> readOne() {
        try {
            return Optional.ofNullable(
                    currentRecipeRestClient.get()
                            .retrieve().toEntity(RecipeDto.class).getBody()
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Collection<RecipeDto> readAll() {
        return Arrays.asList(
                Objects.requireNonNull(recipeRestClient.get()
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(RecipeDto[].class)
                        .getBody())
        );
    }

    public Collection<IngredientDto> readRecipeIngredients(long id) {
        return Arrays.asList(
                Objects.requireNonNull(currentRecipeRestClient.get()
                                .uri("/ingredients")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(IngredientDto[].class)
                        .getBody())
        );
    }

    public void create(RecipeDto data) {
        recipeRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }

    public void update(RecipeDto formData) {
        currentRecipeRestClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    public void addIngredient(IngredientDto formData) {
        currentRecipeRestClient.put()
                .uri("/ingredients/{idIngredient}", Map.of("idIngredient", formData.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    public Collection<RecipeDto> readByPrice(double price) {
        return Arrays.asList(
                Objects.requireNonNull(recipeRestClient.get()
                        .uri("?price={recipePrice}", Map.of("recipePrice", price))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(RecipeDto[].class)
                        .getBody())
        );
    }

    public void deleteCurrent() {
        currentRecipeRestClient.delete()
                .retrieve()
                .toBodilessEntity();
    }
}
