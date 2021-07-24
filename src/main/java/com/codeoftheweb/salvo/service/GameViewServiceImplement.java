package com.codeoftheweb.salvo.service;
import com.codeoftheweb.salvo.dtos.*;
import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.ScoreRepository;
import com.codeoftheweb.salvo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

    @Service
    public class GameViewServiceImplement implements GameViewService {

    @Autowired
    private ScoreRepository repository5scores;

    @Override
    public GameViewDTO makeGameViewDTO(Game game, GamePlayer gamePlayer) {
        State state = getState(gamePlayer);
        Optional<GamePlayer> gpOpponent = Util.getOpponent(gamePlayer.getGame(), gamePlayer.getPlayer());

        if ((state == State.WON || state == State.TIE || state == State.LOST) && gamePlayer.getScoreDto().getScore() == null) {
            if (state == State.WON) {
                repository5scores.save(new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 1.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
                repository5scores.save(new Score(gpOpponent.get().getPlayer(), gamePlayer.getGame(), 0.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
            }
            if (state == State.TIE) {
                repository5scores.save(new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 0.5, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
                repository5scores.save(new Score(gpOpponent.get().getPlayer(), gamePlayer.getGame(), 0.5, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
            }
            if (state == State.LOST) {
                repository5scores.save(new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 0.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
                repository5scores.save(new Score(gpOpponent.get().getPlayer(), gamePlayer.getGame(), 1.0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
            }
        }

        Map<String , Object> hits = getHits(gamePlayer);
        HitDTO hitDTO = new HitDTO(hits);

        GameViewDTO gameViewDTO = new GameViewDTO();
        gameViewDTO.setId(game.getId());
        gameViewDTO.setCreated(game.getCreationDate());
        gameViewDTO.setGameState(state);
        gameViewDTO.setGamePlayers(game.getGamePlayers().stream().map(gp -> new GamePlayerDTO(gp)).collect(Collectors.toSet()));
        gameViewDTO.setShips(gamePlayer.getShips().stream().map(ship->new ShipDTO(ship)).collect(Collectors.toSet()));
        gameViewDTO.setSalvoes(game.getGamePlayers().stream().map(gp -> gp.getSalvos()).flatMap(salvos -> salvos.stream()).map(salvo -> new SalvoDTO(salvo)).collect(Collectors.toSet()));
        gameViewDTO.setHits(hitDTO);

        return gameViewDTO;
    }

    private Map<String, Object> getHits(GamePlayer gp) {
        Optional<GamePlayer> gpOpponent = Util.getOpponent(gp.getGame(), gp.getPlayer());
        Map<String, Object> dto = new LinkedHashMap<>();
        if (gpOpponent.isPresent()) {
            dto.put("self", getHitsDamages(gpOpponent.get()));             //pedimos el mapa y nos pasa en forma de dto
            dto.put("opponent", getHitsDamages(gp));
        } else {
            dto.put("self", new ArrayList<>());
            dto.put("opponent", new ArrayList<>());
        }
        return dto;
    }

    private List<Map<String, Object>> getHitsDamages(GamePlayer gp) {
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

    private List<String> getHitLocations(GamePlayer gp, Salvo salvo){

        GamePlayer gpOpponent = Util.getOpponent(gp.getGame(),gp.getPlayer()).get();
        List<String> shipLocationOpponent = gpOpponent.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
        List<String> hitLocations = salvo.getSalvoLocations().stream().filter(salvoLocation -> shipLocationOpponent.contains(salvoLocation)).collect(Collectors.toList());
        return hitLocations;
    }

    private Map<String, Object> getDamages(GamePlayer gp, List<String> hitsLocations, int[] acumuladorDePosicion) {
        GamePlayer gpOpponent = Util.getOpponent(gp.getGame(), gp.getPlayer()).get();
        List<Ship> ships = gpOpponent.getShips().stream().collect(Collectors.toList());
        int[] cantidadTipo = new int[5];//son los primeros 5 de hits
        Arrays.fill(cantidadTipo, 0, 4, 0);
        for (String location : hitsLocations) {
            int i = 0;
            boolean encontro = false;
            while (i < ships.size() && !encontro) { //cantidad de barcos que tenemos
                int j = 0;
                List<String> shipLocation = ships.get(i).getShipLocations();
                while (j < shipLocation.size() && !encontro) {
                    if (location.equals(shipLocation.get(j))) {
                        int posicion = ships.get(i).getType().ordinal();
                        cantidadTipo[posicion]++;
                        encontro = true;
                    } else {
                        j++;
                    }
                }
                i++;
            }
        }
        for (int i = 0; i < cantidadTipo.length; i++) {
            acumuladorDePosicion[i] = acumuladorDePosicion[i] + cantidadTipo[i];
        }

        return getDamagesMap(cantidadTipo, acumuladorDePosicion);
    }

    private Map<String, Object> getDamagesMap(int[] cantXtipo, int[] acumuladorDePosicion) {
        Map<String, Object> dto = new LinkedHashMap<>();
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

    private int getMissed(List<String> hitsLocations, Salvo salvo) {
        return salvo.getSalvoLocations().size() - hitsLocations.size();
    }

    private State getState(GamePlayer gp) {
        if (gp.getShips().size() != 0){
            Optional<GamePlayer> gpOpponent = Util.getOpponent(gp.getGame(), gp.getPlayer());

            if (!gpOpponent.isPresent()) {
                return State.WAITINGFOROPP;
            }
            if (gpOpponent.get().getShips().size() == 0){
                return State.WAIT;
            }

            List<String> listHitLocations = gp.getSalvos().stream().flatMap(salvo -> getHitLocations(gp, salvo).stream()).collect(Collectors.toList());
            List<String> shipsLocationsOpponent = gpOpponent.get().getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            boolean shipSunk = listHitLocations.size() == shipsLocationsOpponent.size();

            List<String> listHitLocationsOpponent = gpOpponent.get().getSalvos().stream().flatMap(salvo -> getHitLocations(gpOpponent.get(), salvo).stream()).collect(Collectors.toList());
            List<String> shipsLocationsSelf = gp.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            boolean shipSunkOpponent = listHitLocationsOpponent.size() == shipsLocationsSelf.size();

            if (gp.getSalvos().size() == gpOpponent.get().getSalvos().size()) {
                if (gpOpponent.get().getShips().size() > 0) {
                    if (shipSunk && !shipSunkOpponent) {
                        return State.WON;
                    }
                    if (shipSunk && shipSunkOpponent) {
                        return State.TIE;
                    }
                    if (!shipSunk && shipSunkOpponent) {
                        return State.LOST;
                    }
                }
            }
            if (gp.getSalvos().size() > gpOpponent.get().getSalvos().size()) {
                return State.WAIT;
            }else {
                return State.PLAY;
            }
        }
        return State.PLACESHIPS;
    }


}








