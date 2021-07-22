package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShipType { //el enum representa un grupo de constantes.

    @JsonProperty("carrier")
    CARRIER(0),
    @JsonProperty("battleship")
    BATTLESHIP(1),
    @JsonProperty("submarine")
    SUBMARINE(2),
    @JsonProperty("destroyer")
    DESTROYER(3),
    @JsonProperty("patrolboat")
    PATROLBOAT(4);

    private int value;
    ShipType(int value){
        this.value = value;
    }

    private int getValue(){
        return value;
    }



}