package cz.cvut.fit.tjv.recipe_client.api_client;

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
                .baseUrl(baseUrl + "/recipes/{id}")
                .defaultUriVariables(Map.of("id", id))
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
//        } catch (RestClientResponseException e) {
//            if (e.getStatusCode().)
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
}
