package com.codeoftheweb.salvo.service;
import com.codeoftheweb.salvo.dtos.GameViewDTO;
import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;

public interface GameViewService {

     GameViewDTO makeGameViewDTO(Game game, GamePlayer gamePlayer);

}
