package com.codeoftheweb.salvo.dtos;


import com.codeoftheweb.salvo.models.Salvo;

import java.util.List;

public class SalvoDTO {

    private int turn;

    private Long player;

    private List<String> salvoLocations;

    public SalvoDTO() {
    }

    public SalvoDTO(Salvo salvo){
        this.turn = salvo.getTurn();
        this.player = salvo.getGamePlayer().getPlayer().getId();
        this.salvoLocations = salvo.getSalvoLocations();
    }

    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }
}
