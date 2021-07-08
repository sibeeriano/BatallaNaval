package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")//el request mapping evita conflictos de ruta, agregando un prefijo dif para las bases de datos ej,/api evitamos que se pisen las rutas ejemplo games.js, games.html, etc
public class SalvoController {

    @Autowired//crea una instancia unica de esta clase para que sea reutilizada por otras clase./ es parte de bean
    private GameRepository repository1;

    @Autowired//si no hay una instancia de clase, este la crea por primera vez
    private GamePlayerRepository gamePlayerRepository;


//como tenemos el requestMapping/api la direccion va a ser /api/games.
    @GetMapping(value = "/games")   //Rest controller//un servicio de json que devuelve datos no una vista.
    public Map<String , Object> getListGames(){ // si recibe un get de games usamos el metodo para llamarlos y devuelve una instancia de games en formato json.
        Map<String , Object> dto = new LinkedHashMap<>();
        List<Game> games= repository1.findAll(); //primero usamos el metodo de repositorioFindAll, devuelve un conjunto.
        dto.put("games",games.stream().map(game -> gameDTO(game)).collect(Collectors.toList()));//usamos STREAM para obtener una cadena, desp map y collect los usamos para que nos devuelvan una lista DTO
        return dto;
    }

    @GetMapping("/game_view/{n}") //getMapping y RequestMapping es lo mismo, creo.
    public Map<String, Object> gameView(@PathVariable Long n) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(n).get();
        Game game = gamePlayer.getGame();
        Map<String, Object> dto = gameDTO(game);
        dto.put("ships", gamePlayer.getShips().stream().map(this::shipDTO).collect(Collectors.toList()) );
        dto.put("salvoes",game.getGamePlayers().stream().map(gp->gp.getSalvos()).flatMap(salvos -> salvos.stream())
                .map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> shipDTO(Ship ship) { //DTO=Data Transfer Object: estructura creada para organizar los datos para transferirlos a otro sistema.
        //map de tipo valor, clave que va a ser shipDTO
        Map<String, Object> dto = new LinkedHashMap<String, Object>(); //el hashmap nos da control para la forma en se van a mostrar los json
        dto.put("type", ship.getType()); //pedimos que nos devuelva estos datos
        dto.put("locations", ship.getLocations());//"
        return dto;//el dto nos devuelve de forma ordenada los datos que requerimos en el constructor
    }

//salvo

        private Map<String, Object> salvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurn());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getLocations());
        return dto;
    }

    private Map<String, Object> gameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(this::gamePlayerDTO).collect(toSet()));
        dto.put("scores",game.getGamePlayers().stream().map(gp-> gp.getScoreDto()).collect(Collectors.toList()));
        return dto;
    }


    private Map<String,Object> playerDTO(Player player){
        Map<String, Object> str = new LinkedHashMap<String,Object>();
        str.put("id", player.getId());
        str.put("email", player.getUserName());
        return str;
    }

    private Map<String, Object> gamePlayerDTO(GamePlayer gp) {
        Map<String, Object> str = new LinkedHashMap<String, Object>();
        str.put("id", gp.getId());
        str.put("player", playerDTO(gp.getPlayer()));
        return str;
    }


    }



////dto CREAMOS APIS Y CON EL DTO LE DAMOS FORMATO

//REST APIS DECUELVE DATOS EN FORMATO JSON PARA SER VISUALISADOS (NO UNA VISTA SINO DATOS)
//TODO ESTO SERIAN LOS REQUEST MAPPING
//REST: CONJUNTO DE FUNCIONES QUE DEVUELVEN UN CONJUNTO DE DATOS ESPECIFICO PARA QUE OTRO LO CONSUMA