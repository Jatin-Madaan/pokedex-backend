package com.pokedex.pokedex.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PokemonInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private Integer baseExperience;
    private Integer height;
    private Integer weight;
    private List<TypeDTO> types;
    private String colour;
    private String description;

    private List<String> abilities;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(Integer baseExperience) {
        this.baseExperience = baseExperience;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<TypeDTO> getTypes() {
        return types;
    }

    public void setTypes(List<TypeDTO> types) {
        this.types = types;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public PokemonInfoDTO(Integer id, String name, Integer baseExperience,
                          Integer height, Integer weight, List<TypeDTO> types, String color, String description,
                          List<String> abilities) {
        this.id = id;
        this.name = name;
        this.baseExperience = baseExperience;
        this.height = height;
        this.weight = weight;
        this.types = types;
        this.colour = color;
        this.description = description;
        this.abilities = abilities;
    }

    public PokemonInfoDTO() {
    }
}
