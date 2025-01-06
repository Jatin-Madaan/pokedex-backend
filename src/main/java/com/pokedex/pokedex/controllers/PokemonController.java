package com.pokedex.pokedex.controllers;

import com.pokedex.pokedex.dto.EvolutionPhasesDTO;
import com.pokedex.pokedex.dto.PokemonDTO;
import com.pokedex.pokedex.dto.PokemonInfoDTO;
import com.pokedex.pokedex.exceptions.EvolutionNotFoundException;
import com.pokedex.pokedex.exceptions.InfoNotFoundException;
import com.pokedex.pokedex.services.EvolutionService;
import com.pokedex.pokedex.services.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pokemon")
@CrossOrigin("*")
public class PokemonController {

    private final InfoService infoService;
    private final EvolutionService evolutionService;

    @Autowired
    public PokemonController(InfoService infoService, EvolutionService evolutionService) {
        this.infoService = infoService;
        this.evolutionService = evolutionService;
    }


    @GetMapping("/getByIdOrName/{idOrName}")
    public ResponseEntity<PokemonInfoDTO> getPokemonInfoByIdOrName(@PathVariable String idOrName) throws InfoNotFoundException {
        PokemonInfoDTO pokemonInfoDTO = infoService.getPokemonInfoByIdOrName(idOrName);
        return ResponseEntity.ok(pokemonInfoDTO);
    }

    @GetMapping("/limit/{limit}")
    public ResponseEntity<List<PokemonDTO>> getPokemonList(@PathVariable String limit){
        // count = offset=21&limit=20
        List<PokemonDTO> pokemons = infoService.getPokemonList(limit);
        return ResponseEntity.ok(pokemons);
    }

    @GetMapping("/evolution/{idOrName}")
    public ResponseEntity<List<EvolutionPhasesDTO>> getEvolutionChain(@PathVariable String idOrName) throws InfoNotFoundException, EvolutionNotFoundException {
        List<EvolutionPhasesDTO> evolutionChain = evolutionService.getEvolutionChain(idOrName);
        return ResponseEntity.ok(evolutionChain);
    }
}
