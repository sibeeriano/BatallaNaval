package com.codeoftheweb.salvo;


import net.minidev.json.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository repository1;

    @Autowired
    private PlayerRepository repository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShipRepository repository3ship;

    //private Player players;
//estas son las urls y lo que muestran....


    @GetMapping(value = "/games")
    public Map<String, Object> getListaGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        List<Game> games = repository1.findAll();
        if (authentication != null) {
            Map<String, Object> dtoPlayer = new LinkedHashMap<>();
            Player player = repository.findByUserName(authentication.getName());
            dtoPlayer.put("id", player.getId());
            dtoPlayer.put("email", player.getUserName());
            dto.put("player", dtoPlayer);
        } else {
            dto.put("player", "Guest");
        }
        dto.put("games", games.stream().map(game -> gameDTO(game)).collect(Collectors.toList()));
        return dto;
    }


    @RequestMapping(path = "/game_view/{n}", method = RequestMethod.GET)
    //envia info en la url en la parte de consulta de la url
    public ResponseEntity<Object> gameView(@PathVariable Long n, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(n).get();
        Player player = repository.findByUserName(authentication.getName());
        if (gamePlayer.getPlayer().getId() == player.getId()) {
            Game game = gamePlayer.getGame();
            Map<String, Object> dto = gameDTO(game);
            dto.put("ships", gamePlayer.getShips().stream().map(this::shipDTO).collect(Collectors.toList()));
            dto.put("salvoes", game.getGamePlayers().stream().map(gp -> gp.getSalvos()).flatMap(salvos -> salvos.stream())
                    .map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
                        Map<String, Object> dto2 = new LinkedHashMap<>();
            dto2.put("self", new ArrayList<>());
            dto2.put("opponent", new ArrayList<>());
            dto.put("hits", dto2);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //create game y nuevo jugador
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        Player playerAuthentication = getPlayer(authentication);
        if (playerAuthentication != null) {
            Game game = new Game(LocalDateTime.now());
            repository1.save(game);
            GamePlayer gamePlayer = new GamePlayer(game, playerAuthentication);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private Map<String, Object> makeMap(String key, Object value) { //crea la variable gpid
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Player getPlayer(Authentication authentication) { //verifica si hay algun player logueado
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        } else {
            return (repository.findByUserName(authentication.getName()));
        }
    }
    //una forma mas abreviada de poner post
    @PostMapping("/game/{idGame}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long idGame, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            Game game = repository1.findById(idGame).orElse(null);
            if (game != null) {
                List<Player> players = game.getPlayers();
                if (players.size() == 1) {
                    Player player = repository.findByUserName(authentication.getName());
                    GamePlayer gameplayer = gamePlayerRepository.save(new GamePlayer(game, player));
                    return new ResponseEntity<>(makeMap("gpid", gameplayer.getId()), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(makeMap("error", "Juego lleno"), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(makeMap("error", "No hay juego"), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(makeMap("error", "Hay que loguearse"), HttpStatus.UNAUTHORIZED);
        }
    }






    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {
        Map<String , Object> map= new LinkedHashMap<>();
        if (email.isEmpty() || password.isEmpty()) {
            map.put("error","Missing data");
            return new ResponseEntity<>(map,HttpStatus.FORBIDDEN);
        }

        if (repository.findByUserName(email) !=  null) {
            map.put("error","Name already in use");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        repository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//ShipDTO
    private Map<String, Object> shipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }

//salvoDTO

        private Map<String, Object> salvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurn());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getLocations());
        return dto;
    }
//gameDTO
    private Map<String, Object> gameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gameState", "PLACESHIPS");
        dto.put("gamePlayers", game.getGamePlayers().stream().map(this::gamePlayerDTO).collect(toSet()));
        dto.put("scores",game.getGamePlayers().stream().map(gp-> gp.getScoreDto()).collect(Collectors.toList()));
        return dto;
    }

//playerDTO
    private Map<String,Object> playerDTO(Player player){
        Map<String, Object> str = new LinkedHashMap<String,Object>();
        str.put("id", player.getId());
        str.put("email", player.getUserName());
        return str;
    }
//gamplayerDTO
    private Map<String, Object> gamePlayerDTO(GamePlayer gp) {
        Map<String, Object> str = new LinkedHashMap<String, Object>();
        str.put("id", gp.getId());
        str.put("player", playerDTO(gp.getPlayer()));
        return str;
    }








    }








