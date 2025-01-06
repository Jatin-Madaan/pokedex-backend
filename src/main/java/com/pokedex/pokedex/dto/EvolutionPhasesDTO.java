package com.pokedex.pokedex.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class EvolutionPhasesDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String currentSpecies;
    private String previousSpecies;
    private String description;
    private String colour;

    private Integer baseExperience;
    private Integer height;
    private int weight;

    private List<String> abilities;

    public String getDescription() {
        return description;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrentSpecies() {
        return currentSpecies;
    }

    public void setCurrentSpecies(String currentSpecies) {
        this.currentSpecies = currentSpecies;
    }

    public String getPreviousSpecies() {
        return previousSpecies;
    }

    public void setPreviousSpecies(String previousSpecies) {
        this.previousSpecies = previousSpecies;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EvolutionPhasesDTO(Integer id, String currentSpecies, String previousSpecies,
                              String description, String colour, Integer baseExperience,
                              Integer height, int weight, List<String> abilities) {
        this.id = id;
        this.currentSpecies = currentSpecies;
        this.previousSpecies = previousSpecies;
        this.description = description;
        this.colour = colour;
        this.baseExperience = baseExperience;
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
    }

    public EvolutionPhasesDTO() {
    }
}
