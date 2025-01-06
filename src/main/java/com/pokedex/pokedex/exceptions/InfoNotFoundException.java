package com.pokedex.pokedex.exceptions;

public class InfoNotFoundException extends RuntimeException{
    public InfoNotFoundException(String msg){
        super(msg);
    }
}
