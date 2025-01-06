package com.pokedex.pokedex.dto;

import java.io.Serial;
import java.io.Serializable;

public class PokemonDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PokemonDTO(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public PokemonDTO() {
    }
}
