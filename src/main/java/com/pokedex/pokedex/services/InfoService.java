package com.pokedex.pokedex.services;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pokedex.pokedex.dto.PokemonDTO;
import com.pokedex.pokedex.dto.PokemonInfoDTO;
import com.pokedex.pokedex.dto.TypeDTO;
import com.pokedex.pokedex.exceptions.InfoNotFoundException;
import com.pokedex.pokedex.utils.PokemonTypeColor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class InfoService {
    @Value("${base.url}")
    private String BASE_URL;

    private RestTemplate restTemplate ;
    private PokemonTypeColor pokemonTypeColor;
    private Gson gson;

    private final Logger logger = (Logger) LoggerFactory.getLogger(InfoService.class);

    @Autowired
    public InfoService(RestTemplate restTemplate, PokemonTypeColor pokemonTypeColor, Gson gson){
        this.restTemplate = restTemplate;
        this.pokemonTypeColor = pokemonTypeColor;
        this.gson = gson;
    }


    @Cacheable(value = "PokemonInfo", key = "#idOrName")
    public PokemonInfoDTO getPokemonInfoByIdOrName(String idOrName) throws InfoNotFoundException {
        String responseObj = restTemplate.getForObject(BASE_URL + "pokemon/" + idOrName, String.class);
        PokemonInfoDTO pokemonInfoDTO = new PokemonInfoDTO();

        try{
            if(responseObj == null) {
                pokemonInfoDTO = null;
                throw new InfoNotFoundException("Rest Template giving Null Obj");
            }
            JSONObject jsonObject = new JSONObject(responseObj);
            pokemonInfoDTO.setId(jsonObject.getInt("id"));
            pokemonInfoDTO.setName(jsonObject.getString("name"));
            pokemonInfoDTO.setHeight(jsonObject.getInt("height"));
            pokemonInfoDTO.setWeight(jsonObject.getInt("weight"));
            pokemonInfoDTO.setBaseExperience(jsonObject.getInt("base_experience"));

            JSONArray types = jsonObject.getJSONArray("types");
            JSONArray abilities = jsonObject.getJSONArray("abilities");
            List<TypeDTO> pokemonType = new ArrayList<>();
            List<String> abilitiesList = new ArrayList<>();
            for(int i = 0; i < types.length(); i++){
                JSONObject type = types.getJSONObject(i);
                String name = type.getJSONObject("type").getString("name");

                TypeDTO typeDTO = new TypeDTO();
                typeDTO.setName(name);
                typeDTO.setColor(pokemonTypeColor.getTypeColor(name));

                pokemonType.add(typeDTO);
            }

            for(int i = 0; i < abilities.length(); i++){
                JSONObject ability = abilities.getJSONObject(i).getJSONObject("ability");
                abilitiesList.add(ability.getString("name"));
            }
            pokemonInfoDTO.setAbilities(abilitiesList);
            pokemonInfoDTO.setTypes(pokemonType);
            pokemonInfoDTO.setColour(pokemonTypeColor.getPokemonColorFromAPI(pokemonInfoDTO.getId()));
            pokemonInfoDTO.setDescription(getSpeciesDescription(pokemonInfoDTO.getId()));
            logger.debug("dto received :: " + pokemonInfoDTO);
        }catch (InfoNotFoundException e){
            logger.error("error : {}", e.getMessage());
            throw  e;
        }

        return pokemonInfoDTO;
    }

    private String getSpeciesDescription(Integer id){
        String description = "";
        try{
            Object responseObj = restTemplate.getForObject(BASE_URL + "pokemon-species/" + id, Object.class);
            String jsonInString = new Gson().toJson(responseObj);
            JSONObject jsonObject = new JSONObject(jsonInString);
            JSONArray jsonArray = jsonObject.getJSONArray("flavor_text_entries");
            if(jsonArray.length() > 0){
                int j = 1;
                for(int i = 0; i < j; i++){
                    description = description + jsonArray.getJSONObject(i).getString("flavor_text").replaceAll("\n", " ") + "\n";
                }
            }
        }catch(Exception e){
            logger.error("error : {} : {}", e.getMessage(), id);
        }
        return description;
    }

    @Cacheable(value = "PokemonList", key = "#limit")
    public List<PokemonDTO> getPokemonList(String limit) {
        String responseObj = restTemplate.getForObject(BASE_URL + "pokemon?" + limit, String.class);
        //String jsonInString = gson.toJson(responseObj);
        JSONObject jsonObject = new JSONObject(responseObj);
        JSONArray resultsArray = jsonObject.getJSONArray("results");
        Type type = new TypeToken<List<PokemonDTO>>() {}.getType();
        List<PokemonDTO> list = gson.fromJson(String.valueOf(resultsArray), type);

        for(int i = 0; i < list.size(); i++){
            String url = list.get(i).getUrl();
            int start = url.lastIndexOf("pokemon") + "pokemon".length() + 1;
            list.get(i).setId(Integer.valueOf(url.substring(start, url.length() - 1)));
        }
        return list;
    }
}
