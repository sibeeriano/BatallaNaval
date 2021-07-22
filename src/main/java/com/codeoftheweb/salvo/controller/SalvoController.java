package com.codeoftheweb.salvo.controller;


import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.codeoftheweb.salvo.dtos.*;
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

    @Autowired
    private SalvoRepository salvoRepository;

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
        dto.put("games", games.stream().map(game -> new GameDTO(game)).collect(Collectors.toList()));
        return dto;
    }


    @GetMapping("/game_view/{id}")
    public ResponseEntity<?> getViewGame(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepository.findById(id).get();
        Game game = gamePlayer.getGame();
        Player playerAuthentication = repository.findByUserName(authentication.getName());
        if (gamePlayer.getPlayer().getId() == playerAuthentication.getId()) {
            Map<String , Object> hits = getHits(gamePlayer);
            return new ResponseEntity<>(new GameViewDTO(game, gamePlayer, hits), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(makeMap("error", "Id no autorizado"), HttpStatus.UNAUTHORIZED);
        }
    }


    //create game y nuevo jugador
    @PostMapping(path = "/game/{nn}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long nn, Authentication authentication) {
        Player playerAuthentication = getPlayer(authentication);

        if (playerAuthentication != null) {
            Game game = repository1.findById(nn).get();
            if (game != null) {
                if (game.getPlayers().size() == 1) {
                    Player player = repository.findByUserName(authentication.getName());
                    GamePlayer gamePlayer = new GamePlayer(game, player);
                    gamePlayerRepository.save(gamePlayer);
                    return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(makeMap("error", "El juego esta lleno"), HttpStatus.FORBIDDEN);
                }

            } else {
                return new ResponseEntity<>(makeMap("error", "No hay tal juego"), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(makeMap("error", "Imposible unirse al Juego"), HttpStatus.UNAUTHORIZED);
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


    @PostMapping(path = "/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayerId, @RequestBody List<Ship> ships, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
            if (gamePlayer != null) {
                Player player = repository.findByUserName(authentication.getName());
                if (player.getId() == gamePlayer.getPlayer().getId()) {
                    if (gamePlayer.getShips().size() == 0) {
                        if (ships.size() == 5) {
                            for (Ship ship : ships) {
                                repository3ship.save(new Ship(ship.getType(), ship.getLocations(), gamePlayer));
                            }
                            return new ResponseEntity<>(makeMap("OK", "Barcos guardados correctamente"), HttpStatus.CREATED);
                        } else {
                            return new ResponseEntity<>(makeMap("error", "Tenes que poner 5 barcos!"), HttpStatus.FORBIDDEN);
                        }
                    } else {
                        return new ResponseEntity<>(makeMap("error", "los barcos estan en posicion"), HttpStatus.FORBIDDEN);
                    }
                } else {
                    return new ResponseEntity<>(makeMap("error", "No sos el usuario de esta cuenta"), HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(makeMap("error", "no existe el usuario"), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(makeMap("error", "Necesita loguearse"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (email.isEmpty() || password.isEmpty()) {
            map.put("error", "Missing data");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }
        if (repository.findByUserName(email) != null) {
            map.put("error", "Name already in use");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }
        repository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        Player playerAuthentication = getPlayer(authentication);
        if (playerAuthentication != null) {
            Game game = new Game();
            GamePlayer gamePlayer = new GamePlayer(game, playerAuthentication);
            repository1.save(game);
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping(path = "/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> addSalvos(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Salvo salvo) {
        Player playerAuthentication = getPlayer(authentication);

        if (playerAuthentication == null) {
            return new ResponseEntity<>(makeMap("error", "User ID not authorized"), HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gp1 = gamePlayerRepository.findById(gamePlayerId).orElse(null);

        if (gp1 == null) {
            return new ResponseEntity<>(makeMap("error", "ID is not valid"), HttpStatus.UNAUTHORIZED);
        }
        Game game = gp1.getGame();

        if (gp1.getPlayer().getId() != playerAuthentication.getId()) {
            return new ResponseEntity<>(makeMap("error", "ID does not correspond to the player"), HttpStatus.UNAUTHORIZED);
        }

        Optional<GamePlayer> gp2 = getOpponent(game, playerAuthentication);
        if (gp2 == null) {
            return new ResponseEntity<>(makeMap("error", "Two players are required"), HttpStatus.FORBIDDEN);
        }

        if (salvo.getSalvoLocations().size() < 1 || salvo.getSalvoLocations().size() > 5) {
            return new ResponseEntity<>(makeMap("error", "this amount is not allowed"), HttpStatus.FORBIDDEN);
        }

        if (gp1.getSalvos().size() == gp2.get().getSalvos().size()) {
            salvoRepository.save(new Salvo(gp1.getSalvos().size() + 1, salvo.getSalvoLocations(), gp1));
            return new ResponseEntity<>(makeMap("OK", "Salvo saved correctly"), HttpStatus.OK);
        } else {
            if (gp1.getSalvos().size() > gp2.get().getSalvos().size()) {
                return new ResponseEntity<>(makeMap("error", "Non-corresponded turn"), HttpStatus.FORBIDDEN);
            } else {
                salvoRepository.save(new Salvo(gp1.getSalvos().size() + 1, salvo.getSalvoLocations(), gp1));
                return new ResponseEntity<>(makeMap("OK", "Salvo saved correctly"), HttpStatus.OK);
            }
        }

    }

    //oponente
    private Optional<GamePlayer> getOpponent(Game game, Player playerAuthentication) {
        Optional<GamePlayer> gamePlayerOpponent = game.getGamePlayers().stream().filter(gp -> gp.getPlayer().getId() != playerAuthentication.getId()).findFirst();
        return gamePlayerOpponent;
    }

    //tarea 10
//
    private Map<String , Object> getHits(GamePlayer gp){
        Optional<GamePlayer> gpOpponent = getOpponent(gp.getGame(),gp.getPlayer());
        Map<String , Object> dto = new LinkedHashMap<>();
        if(gpOpponent.isPresent()) {
            dto.put("self",getHitsDamages(gpOpponent.get()));             //pedimos el mapa y nos pasa en forma de dto
            dto.put("opponent",getHitsDamages(gp));
        }else{
            dto.put("self",new ArrayList<>());
            dto.put("opponent",new ArrayList<>());
        }
        return dto;
    }


//
    private List<String> getHitLocations(GamePlayer gp, Salvo salvo){
        List<String> hitLocations = new ArrayList<>();
        GamePlayer gpOpponent = getOpponent(gp.getGame(),gp.getPlayer()).get();
        List<Ship> shipsOpponent = gpOpponent.getShips().stream().collect(Collectors.toList());
        for(String salvoLocation : salvo.getSalvoLocations()){
            int i = 0;
            boolean encontro = false;
            while(i < shipsOpponent.size() && !encontro){
                int j = 0;
                List<String> shipLocations = shipsOpponent.get(i).getLocations();
                while(j < shipLocations.size() && !encontro){
                    if(salvoLocation.equals(shipLocations.get(j))){
                        hitLocations.add(salvoLocation);
                        encontro=true;
                    }else{
                        j++;
                    }
                }
                i++;
            }
        }
        return hitLocations;
    }



    //
    private Map<String , Object> getDamages(GamePlayer gp,List<String> hitsLocations,int[] acumuladorDePosicion){
        GamePlayer gpOpponent = getOpponent(gp.getGame(),gp.getPlayer()).get();
        List<Ship> ships = gpOpponent.getShips().stream().collect(Collectors.toList());
        int[] cantidadTipo = new int[5];//son los primeros 5 de hits
        Arrays.fill(cantidadTipo,0,4,0);
        for(String location : hitsLocations){
            int i = 0;
            boolean encontro = false;
            while(i < ships.size() && !encontro){ //cantidad de barcos que tenemos
                int j = 0;
                List<String> shipLocation = ships.get(i).getLocations();
                while(j < shipLocation.size() &&  !encontro){
                    if(location.equals(shipLocation.get(j))){
                        int posicion = ships.get(i).getType().ordinal();
                        cantidadTipo[posicion]++;
                        encontro = true;
                    }else{
                        j++;
                    }
                }
                i++;
            }
        }
        for(int i = 0; i < cantidadTipo.length ; i++){
            acumuladorDePosicion[i] = acumuladorDePosicion[i] + cantidadTipo[i];
        }

        return getDamagesMap(cantidadTipo,acumuladorDePosicion);
    }

    private Map<String , Object> getDamagesMap(int[] cantXtipo,int[] acumuladorDePosicion){
        Map<String , Object> dto = new LinkedHashMap<>();
        dto.put("carrierHits", cantXtipo[0]);
        dto.put("battleshipHits", cantXtipo[1]);
        dto.put("submarineHits", cantXtipo[2]);
        dto.put("destroyerHits", cantXtipo[3]);
        dto.put("patrolboatHits", cantXtipo[4]);
        dto.put("carrier", acumuladorDePosicion[0]);
        dto.put("battleship", acumuladorDePosicion[1]);
        dto.put("submarine", acumuladorDePosicion[2]);
        dto.put("destroyer", acumuladorDePosicion[3]);
        dto.put("patrolboat", acumuladorDePosicion[4]);

        return dto;
    }

    //agua
    private int getMissed(List<String> hitsLocations,Salvo salvo){
        return salvo.getSalvoLocations().size() - hitsLocations.size();
    }


//esto guarda lo logica anterior por turnos
    private List<Map<String,Object>> getHitsDamages(GamePlayer gp) {
        List<Map<String, Object>> map = new ArrayList<>();
        int[] acumuladorDePosicion = new int[5]; //arreglo de tipo entero de 5 posiciones que se van a llenar con enteros, y cada posicion va a ser un barco
        Arrays.fill(acumuladorDePosicion, 0, 4, 0); //inicializa el array en 0 para que no arranqeue como null
        for (Salvo salvo : gp.getSalvos()) {
            Map<String, Object> dto = new LinkedHashMap<>();//recorro todos los salvos y voy guardando la info en los dtos
            dto.put("turn", salvo.getTurn());
            List<String> hitsLocations = getHitLocations(gp, salvo);
            dto.put("hitLocations", hitsLocations);
            dto.put("damages", getDamages(gp, hitsLocations, acumuladorDePosicion));
            dto.put("missed", getMissed(hitsLocations, salvo));
            map.add(dto);
        }
        return map;

    }




}



    /*

//ShipDTO TODO ESTO SE REEMPLAZO POR EL PACKAGE DTO
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






*/










