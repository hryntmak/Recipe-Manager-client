package cz.cvut.fit.tjv.recipe_client.api_client;

import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class IngredientClient {
    private String baseUrl;
    private RestClient ingredientRestClient;
    private RestClient currentIngredientRestClient;

    public IngredientClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        ingredientRestClient = RestClient.create(baseUrl + "/ingredients");
    }

    public void setCurrentIngredient(long id) {
        currentIngredientRestClient = RestClient.builder()
                .baseUrl(baseUrl + "/ingredients/{id}")
                .defaultUriVariables(Map.of("id", id))
                .build();
    }

    public Optional<IngredientDto> readOne() {
        try {
            return Optional.ofNullable(
                    currentIngredientRestClient.get()
                            .retrieve().toEntity(IngredientDto.class).getBody()
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Collection<IngredientDto> readAll() {
        return Arrays.asList(
                Objects.requireNonNull(ingredientRestClient.get()
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(IngredientDto[].class)
                        .getBody())
        );
    }

    public void create(IngredientDto data) {
        ingredientRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }

    public void update(IngredientDto formData) {
        currentIngredientRestClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteCurrent() {
        currentIngredientRestClient.delete()
                .retrieve()
                .toBodilessEntity();
    }
}
