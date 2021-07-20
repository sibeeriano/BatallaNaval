package com.codeoftheweb.salvo;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Date;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String userName;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER) //hace la relacion con game player
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER) //
    Set<Score> scores = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER) //
    Set<Score> ships = new HashSet<>();

    public Player(){}



    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public void setUserName(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public void AddGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Score getScore(Game game) {
        return this.scores.stream().filter(score -> score.getGame().getId()== game.getId()).findFirst().orElse(null);
    }



}








