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
public class IngredientClient {
    private String baseUrl;
    private RestClient recipeRestClient;
    private RestClient currentRecipeRestClient;

    public IngredientClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        recipeRestClient = RestClient.create(baseUrl + "/ingredients");
    }

    public void setCurrentRecipe(long id) {
        currentRecipeRestClient = RestClient.builder()
                .baseUrl(baseUrl + "/ingredients/{id}")
                .defaultUriVariables(Map.of("id", id))
                .build();
    }

    public Optional<IngredientDto> readOne() {
        try {
            return Optional.ofNullable(
                    currentRecipeRestClient.get()
                            .retrieve().toEntity(IngredientDto.class).getBody()
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Collection<IngredientDto> readAll() {
        return Arrays.asList(
                Objects.requireNonNull(recipeRestClient.get()
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(IngredientDto[].class)
                        .getBody())
        );
    }

    public void create(IngredientDto data) {
        recipeRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }

    public void update(IngredientDto formData) {
        currentRecipeRestClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }
}
