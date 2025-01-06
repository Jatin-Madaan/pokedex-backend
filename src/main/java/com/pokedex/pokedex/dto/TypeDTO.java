package com.pokedex.pokedex.dto;

import java.io.Serializable;

public class TypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TypeDTO(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public TypeDTO() {
    }
}
