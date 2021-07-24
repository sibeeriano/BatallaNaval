package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.State;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GameViewDTO {

    private Long id;

    private LocalDateTime created;

    private State gameState;

    private Set<GamePlayerDTO> gamePlayers = new HashSet<>();

    private Set<ShipDTO> ships = new HashSet<>();

    private Set<SalvoDTO> salvoes = new HashSet<>();

    private HitDTO hits;

    public GameViewDTO() {
    }

    /*public GameViewDTO(Game game, GamePlayer gamePlayer, Map<String, Object> map, State state){
        this.id = game.getId();
        this.created = game.getCreationDate();
        this.gameState = state;
        this.gamePlayers = game.getGamePlayers().stream().map(gp -> new GamePlayerDTO(gp)).collect(Collectors.toSet());
        this.ships = gamePlayer.getShips().stream().map(ship->new ShipDTO(ship)).collect(Collectors.toSet());
        this.salvoes = game.getGamePlayers().stream().map(gp -> gp.getSalvos()).flatMap(salvos -> salvos.stream()).map(salvo -> new SalvoDTO(salvo)).collect(Collectors.toSet());
        this.hits = new HitDTO(map);
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public State getGameState() {
        return gameState;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public Set<GamePlayerDTO> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayerDTO> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<ShipDTO> getShips() {
        return ships;
    }

    public void setShips(Set<ShipDTO> ships) {
        this.ships = ships;
    }

    public Set<SalvoDTO> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<SalvoDTO> salvoes) {
        this.salvoes = salvoes;
    }

    public HitDTO getHits() {
        return hits;
    }

    public void setHits(HitDTO hits) {
        this.hits = hits;
    }
}
