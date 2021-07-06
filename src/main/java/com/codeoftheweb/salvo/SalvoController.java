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
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository repository1;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;
//estas son las urls y lo que muestran....


    @RequestMapping("/games")
    public Set<Map<String, Object>> getGames() {
        return repository1.findAll().stream().map(this::gameDTO).collect(toSet());
    }

    @GetMapping("/game_view/{n}")
    public Map<String, Object> gameView(@PathVariable Long n) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(n).get();
        Game game = gamePlayer.getGame();
        Map<String, Object> dto = gameDTO(game);
        dto.put("ships", gamePlayer.getShips().stream().map(this::shipDTO).collect(Collectors.toList()) );
        dto.put("salvoes",game.getGamePlayers().stream().map(gp->gp.getSalvos()).flatMap(salvos -> salvos.stream())
                .map(salvo -> salvoDTO(salvo)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> shipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
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




//@GetMapping("/api/employees/{id}")
//@ResponseBody
//public String getEmployeesById(@PathVariable String id) {
//    return "ID: " + id;
//}



