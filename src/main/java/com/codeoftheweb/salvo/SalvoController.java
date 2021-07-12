package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
//estas son las urls y lo que muestran....


    @GetMapping(value = "/games")
        public Map<String , Object> getListaGames(Authentication authentication){
        Map<String , Object> dto = new LinkedHashMap<>();
        List<Game> games= repository1.findAll();
        if(authentication != null){
            Map<String , Object> dtoPlayer = new LinkedHashMap<>();
            Player player = repository.findByUserName(authentication.getName());
            dtoPlayer.put("id", player.getId());
            dtoPlayer.put("email",player.getUserName());
            dto.put("player",dtoPlayer);
        }else{
            dto.put("player","Guest");
        }
        dto.put("games",games.stream().map(game -> gameDTO( game)).collect(Collectors.toList()));
        return dto;
    }



    @RequestMapping(path = "/game_view/{n}", method = RequestMethod.GET)
    public ResponseEntity<Object> gameView(@PathVariable Long n, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(n).get();
        Player player = repository.findByUserName(authentication.getName());
        if (gamePlayer.getPlayer().getId() == player.getId()) {
            Game game = gamePlayer.getGame();
            Map<String, Object> dto = gameDTO(game);
            dto.put("ships", gamePlayer.getShips().stream().map(this::shipDTO).collect(Collectors.toList()) );
            dto.put("salvoes",game.getGamePlayers().stream().map(gp->gp.getSalvos()).flatMap(salvos -> salvos.stream())
            .map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
            return new ResponseEntity<>(dto,HttpStatus.OK);
            }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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








