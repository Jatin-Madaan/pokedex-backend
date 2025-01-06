package com.pokedex.pokedex.services;


import com.google.gson.Gson;
import com.pokedex.pokedex.dto.EvolutionPhasesDTO;
import com.pokedex.pokedex.dto.PokemonInfoDTO;
import com.pokedex.pokedex.exceptions.EvolutionNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EvolutionService {
    @Value("${base.url}")
    private String BASE_URL;

    private final RestTemplate restTemplate;
    
    private final InfoService infoService;
    private final Gson gson;

    @Autowired
    public EvolutionService(RestTemplate restTemplate, Gson gson, InfoService infoService){
        this.restTemplate = restTemplate;
        this.gson = gson;
        this.infoService = infoService;
    }

    // Fetch Evolution Chain by Pokémon National ID
    @Cacheable(value = "EvolutionChain", key = "#pokemonIdOrName")
    public List<EvolutionPhasesDTO> getEvolutionChain(String pokemonIdOrName) throws EvolutionNotFoundException {
        String url = BASE_URL + "pokemon/" + pokemonIdOrName.toLowerCase();

        // Fetching Pokémon data to get evolution chain URL
        PokemonResponse pokemonResponse = restTemplate.getForObject(url, PokemonResponse.class);
        Object evolutionChain = null;
        List<EvolutionPhasesDTO> evolutionPhases = new ArrayList<>();

        if (pokemonResponse != null && pokemonResponse.getSpecies() != null) {
            // Fetch Evolution chain data using species URL
            evolutionChain = restTemplate.getForObject(pokemonResponse.getSpecies().getUrl(), Object.class);
            if(evolutionChain != null){
                String jsonInString = gson.toJson(evolutionChain);
                JSONObject evolution_chain = new JSONObject(jsonInString);
                String EVOLUTION_URL = evolution_chain.getJSONObject("evolution_chain").getString("url");
                String chain = restTemplate.getForObject(EVOLUTION_URL, String.class);
                String prev_state = "";
                JSONObject chainJson = new JSONObject(chain);
                chainJson = chainJson.getJSONObject("chain");

                getPhases(chainJson, evolutionPhases, prev_state);

            }else{
                throw new EvolutionNotFoundException("Evolution chain not found for this id : " + pokemonIdOrName);
            }
        }

        return evolutionPhases;
    }

    private void getPhases(JSONObject chain, List<EvolutionPhasesDTO> phases, String prev_state) throws EvolutionNotFoundException {
        if(chain == null) return;

        String curr_state = chain.getJSONObject("species").getString("name");
        Integer pokemonId = null;
        String desc = "";
        String colour = "";
        Integer height = null;
        Integer weight = null;
        Integer experience = null;
        List<String> abilities = null;
        PokemonInfoDTO info = infoService.getPokemonInfoByIdOrName(curr_state);
        if(info != null) {
            pokemonId = info.getId();
            desc = info.getDescription();
            colour = info.getColour();
            height = info.getHeight();
            weight = info.getWeight();
            experience = info.getBaseExperience();
            abilities = info.getAbilities();
        }
        phases.add(new EvolutionPhasesDTO(pokemonId, curr_state, prev_state,desc, colour, experience, height, weight, abilities));

        JSONArray evolvesTo = chain.getJSONArray("evolves_to");
        if(evolvesTo.length() > 0){
            getPhases(evolvesTo.getJSONObject(0), phases, curr_state);
        }
    }

    // Inner classes to represent Pokémon and Evolution Chain response from PokeAPI
    static class PokemonResponse {
        private Species species;

        public Species getSpecies() {
            return species;
        }

        public void setSpecies(Species species) {
            this.species = species;
        }

        static class Species {
            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
