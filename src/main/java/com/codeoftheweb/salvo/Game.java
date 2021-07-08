package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator; //Implementacion JPA que asigna tablas de datos
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity //le dice a spring que cree una tabla de Game para esta clase
public class Game {

    @Id //contiene un id para esta clase en la data base
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //generador de id
    @GenericGenerator(name = "native", strategy = "native") //generador id
    private Long id; //le decimos al jpa que persista todas las variables de instancia como columnas en l abase de datos

    private LocalDateTime creationDate;


    @OneToMany(mappedBy="game", fetch=FetchType.EAGER) //relacion uno a muchos/un juego para muchos jugadores/fetch le dice a jpa que cuando se cargue un juego se cargue el id del jugador.
    private Set<GamePlayer> gamePlayers = new HashSet<>();// seteamos el propietario de la relacion.
    //usamos set para que jpa no devuelva datos duplicados de las tablas de datos


    @OneToMany(mappedBy="game", fetch=FetchType.EAGER) //un juego varios puntajes
    private Set<Score> scores = new HashSet<>();



    public Game() {}

    public Game(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void AddGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }



    public Long getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }


}









