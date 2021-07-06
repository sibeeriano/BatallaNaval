package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private int turn; //cree la variable para tipo de salvo

    @ElementCollection
    @Column(name = "SalvoLocations") //es el nombre de la tabla de la base de datos
    private List<String> locations; //genera la lista de las locaciones

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer ;

    public Salvo() {}

    public Salvo(int turn, List<String> locations, GamePlayer gamePlayer) {
        this.turn = turn;
        this.locations = locations;
        this.gamePlayer = gamePlayer;

    }

    public int getTurn() {
        return turn;
    }
    public void setTurn(int type) {
        this.turn = type;
    }

    public List<String> getLocations() {
        return locations;
    }
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }




    public Long getId() {
        return id;
    }










}
