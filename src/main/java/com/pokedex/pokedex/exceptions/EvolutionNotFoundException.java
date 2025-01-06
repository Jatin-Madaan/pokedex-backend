package com.pokedex.pokedex.exceptions;

public class EvolutionNotFoundException extends RuntimeException{
    public EvolutionNotFoundException(String msg){
        super(msg);
    }
}
