package com.codeoftheweb.salvo;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
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

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER) //hace la relacion con game player
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER) //
    Set<Score> scores = new HashSet<>();

    public Player(){}

    public Player(String userName) {
        this.userName = userName;
    }

  //  @JsonIgnore
  //  public List<Game> getGames(){
  //      return gamePlayers.stream().map(GamePlayer::getGame).collect(Collectors.toList());
  //  }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    public void AddGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }


    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Long getId() {
        return id;
    }

    public Score getScore(Game game) {
        return this.scores.stream().filter(score -> score.getGame().getId()== game.getId()).findFirst().orElse(null);
    }
}








