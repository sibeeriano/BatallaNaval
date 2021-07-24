package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER) //
    private Set<Ship> ships =  new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER) //
    private Set<Salvo> salvos =  new HashSet<>();



   /* public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship); //se agrega a la lista de ships
    }*/

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.creationDate = LocalDateTime.of(LocalDate.now(), LocalTime.now());

    }



    public Long getId() {
        return id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public Score getScoreDto() {

        Score score = this.getPlayer().getScore(this.getGame());
        if (score == null) {
            score= new Score();
            score.setPlayer(this.getPlayer());
            score.setScore(null);
        }
        return score;
    }

}







