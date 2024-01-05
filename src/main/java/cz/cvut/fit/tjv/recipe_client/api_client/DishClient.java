package cz.cvut.fit.tjv.recipe_client.api_client;

import cz.cvut.fit.tjv.recipe_client.model.DishDto;
import cz.cvut.fit.tjv.recipe_client.model.IngredientDto;
import cz.cvut.fit.tjv.recipe_client.model.RecipeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class DishClient {
    private String baseUrl;
    private RestClient dishRestClient;
    private RestClient currentDishRestClient;

    public DishClient(@Value("${api.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        dishRestClient = RestClient.create(baseUrl + "/dishes");
    }

    public void setCurrentDish(long id) {
        currentDishRestClient = RestClient.builder()
                .baseUrl(baseUrl + "/recipes/{idDish}")
                .defaultUriVariables(Map.of("idDish", id))
                .build();
    }

    public Optional<DishDto> readOne() {
        try {
            return Optional.ofNullable(
                    currentDishRestClient.get()
                            .retrieve().toEntity(DishDto.class).getBody()
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public Collection<DishDto> readAll() {
        return Arrays.asList(
                Objects.requireNonNull(dishRestClient.get()
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(DishDto[].class)
                        .getBody())
        );
    }

    public void create(DishDto data) {
        dishRestClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }

    public void update(DishDto formData) {
        currentDishRestClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteCurrent() {
        currentDishRestClient.delete()
                .retrieve()
                .toBodilessEntity();
    }
}
