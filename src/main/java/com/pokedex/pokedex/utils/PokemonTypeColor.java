package com.pokedex.pokedex.utils;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class PokemonTypeColor {

    @Value("${base.url}")
    private String BASE_URL;

    private final RestTemplate restTemplate;

    private final Logger logger = (Logger) LoggerFactory.getLogger(PokemonTypeColor.class);

    @Autowired
    public PokemonTypeColor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getPokemonColorFromAPI(int pokemonId) {
        String apiUrl = BASE_URL + "pokemon-species/" + pokemonId;
        Object responseObj = restTemplate.getForObject(apiUrl, Object.class);
        String color = "#F0F0F0";
        try {
            String jsonInString = new Gson().toJson(responseObj);
            JSONObject jsonObject = new JSONObject(jsonInString);
            color = jsonObject.getJSONObject("color").get("name").toString();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        return color;
    }

    public String getTypeColor(String type) {
        Map<String, String> typeColors = new HashMap<>();

        // Pokémon Type -> Color Mapping
        typeColors.put("normal", "#A8A878"); // Beige / Light Brown
        typeColors.put("fighting", "#C03028"); // Red
        typeColors.put("flying", "#A890F0"); // Light Purple
        typeColors.put("poison", "#A040A0"); // Purple
        typeColors.put("ground", "#E0C068"); // Brownish Yellow
        typeColors.put("rock", "#B8A038"); // Dark Yellowish-Brown
        typeColors.put("bug", "#A8B820"); // Olive Green
        typeColors.put("ghost", "#705898"); // Purple / Dark Violet
        typeColors.put("steel", "#B8B8D0"); // Steel Gray
        typeColors.put("fire", "#F08030"); // Orange
        typeColors.put("water", "#6890F0"); // Blue
        typeColors.put("grass", "#78C850"); // Green
        typeColors.put("electric", "#F8D030"); // Yellow
        typeColors.put("psychic", "#F85888"); // Pinkish Red
        typeColors.put("ice", "#98D8D8"); // Ice Blue
        typeColors.put("dragon", "#7038F8"); // Purple
        typeColors.put("dark", "#705848"); // Dark Brown
        typeColors.put("fairy", "#F0B6BC"); // Light Pink

        // Return the corresponding color for the given Pokémon type
        return typeColors.get(type);
    }
}
