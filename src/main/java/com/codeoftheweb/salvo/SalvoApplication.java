package com.codeoftheweb.salvo;

import org.apache.tomcat.jni.Local;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


@Bean
public CommandLineRunner initData(PlayerRepository repository, GameRepository repository1, GamePlayerRepository repository2, ShipRepository repository3ship, SalvoRepository repository4salvo, ScoreRepository repository5scores) {
	return (args) -> {
		//games
		Game game1 = repository1.save(new Game(LocalDateTime.of(LocalDate.now(), LocalTime.now())));
		Game game2 = repository1.save(new Game(LocalDateTime.of(LocalDate.now(), LocalTime.now().plusHours(1))));
		Game game3 = repository1.save(new Game(LocalDateTime.of(LocalDate.now(), LocalTime.now().plusHours(2))));

//Players
		Player player1 =repository.save(new Player(" j.bauer@ctu.gov", "1234"));
		Player player2 =repository.save(new Player(" c.obrian@ctu.gov", "12345"));
		Player player3 =repository.save(new Player(" t.almeida@ctu.gov", "123456"));
		Player player4 =repository.save(new Player(" j.bauer2@ctu.gov", "1234567"));
		Player player5 =repository.save(new Player(" d.palmer@whitehouse.gov", "123456789"));

//game1
		GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
		repository2.save(gamePlayer1);
		GamePlayer gamePlayer2 = new GamePlayer(game1, player2);
		repository2.save(gamePlayer2);
//game2
		GamePlayer gamePlayer3 = new GamePlayer(game2, player3);
		repository2.save(gamePlayer3);
		GamePlayer gamePlayer4 = new GamePlayer(game2, player4);
		repository2.save(gamePlayer4);
//game3
		GamePlayer gamePlayer5 = new GamePlayer(game3, player5);
		repository2.save(gamePlayer5);
		GamePlayer gamePlayer6 = new GamePlayer(game3, player2);
		repository2.save(gamePlayer6);

										//**game 1**//

//game1 ships player1
		repository3ship.save(new Ship("Destroyer", List.of("H2", "H3", "H4"),gamePlayer1));
		repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"),gamePlayer1));
		repository3ship.save(new Ship("Carrier", List.of("B2", "B3", "B4", "B5", "B6"),gamePlayer1));
		repository3ship.save(new Ship("Patrol", List.of("E2", "F2"),gamePlayer1));
		repository3ship.save(new Ship("BattleShip", List.of("A7", "B7", "C7", "D7"),gamePlayer1));
//game1 ships player2
		repository3ship.save(new Ship("Destroyer", List.of("H2", "H3", "H4"),gamePlayer2));
		repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"),gamePlayer2));
		repository3ship.save(new Ship("Carrier", List.of("B2", "B3", "B4", "B5", "B6"),gamePlayer2));
		repository3ship.save(new Ship("Patrol", List.of("E2", "F2"),gamePlayer2));
		repository3ship.save(new Ship("BattleShip", List.of("A7", "B7", "C7", "D7"),gamePlayer2));

//game1Salvoes
		repository4salvo.save(new Salvo(1, List.of("H2", "H3", "H4", "B3", "B8"), gamePlayer1));
		repository4salvo.save(new Salvo(1, List.of("E2", "B3", "J4", "A3", "D8"), gamePlayer2));

//score 1
		repository5scores.save(new Score(player1, game1, 0,LocalDateTime.of(LocalDate.now(), LocalTime.now())));
		repository5scores.save(new Score(player2, game1, 1,LocalDateTime.of(LocalDate.now(), LocalTime.now())));

										//**game 2**//

//game2 ships player3
		repository3ship.save(new Ship("Destroyer", List.of("H2", "I2", "J2"),gamePlayer3));
		repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"),gamePlayer3));
		repository3ship.save(new Ship("Carrier", List.of("C2", "C3", "C4", "C5", "C6"),gamePlayer3));
		repository3ship.save(new Ship("Patrol", List.of("E9", "E10"),gamePlayer3));
		repository3ship.save(new Ship("BattleShip", List.of("E10", "F10", "G10", "H10"),gamePlayer3));
//game2 ships player4
		repository3ship.save(new Ship("Destroyer", List.of("j2", "K2", "L2"),gamePlayer4));
		repository3ship.save(new Ship("Submarine", List.of("J4", "J5", "J6"),gamePlayer4));
		repository3ship.save(new Ship("Carrier", List.of("C2", "C3", "C4", "C5", "C6"),gamePlayer4));
		repository3ship.save(new Ship("Patrol", List.of("H9", "H10"),gamePlayer4));
		repository3ship.save(new Ship("BattleShip", List.of("B10", "C10", "D10", "E10"),gamePlayer4));

//game2Salvoes
		repository4salvo.save(new Salvo(1, List.of("C2", "I3", "D4", "F3", "E8"), gamePlayer3));
		repository4salvo.save(new Salvo(1, List.of("B2", "D8", "G4", "F10", "C3"), gamePlayer4));

//score game2
		repository5scores.save(new Score(player3, game2, 0,LocalDateTime.of(LocalDate.now(), LocalTime.now())));
		repository5scores.save(new Score(player4, game2, 1,LocalDateTime.of(LocalDate.now(), LocalTime.now())));

											//**game 3**//

//game3 ships player3
		repository3ship.save(new Ship("Destroyer", List.of("j2", "K2", "L2"),gamePlayer5));
		repository3ship.save(new Ship("Submarine", List.of("J4", "J5", "J6"),gamePlayer5));
		repository3ship.save(new Ship("Carrier", List.of("C2", "C3", "C4", "C5", "C6"),gamePlayer5));
		repository3ship.save(new Ship("Patrol", List.of("H9", "H10"),gamePlayer5));
		repository3ship.save(new Ship("BattleShip", List.of("B10", "C10", "D10", "E10"),gamePlayer5));
//game3 ships player 4
		repository3ship.save(new Ship("Destroyer", List.of("H2", "H3", "H4"),gamePlayer6));
		repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"),gamePlayer6));
		repository3ship.save(new Ship("Carrier", List.of("B2", "B3", "B4", "B5", "B6"),gamePlayer6));
		repository3ship.save(new Ship("Patrol", List.of("E2", "F2"),gamePlayer6));
		repository3ship.save(new Ship("BattleShip", List.of("A7", "B7", "C7", "D7"),gamePlayer6));

//game3Salvos
		repository4salvo.save(new Salvo(1, List.of("H2", "H3", "H4", "B3", "B8"), gamePlayer5));
		repository4salvo.save(new Salvo(1, List.of("E2", "B3", "J4", "A3", "D8"), gamePlayer6));

//score game3
		repository5scores.save(new Score(player2, game3, 1,LocalDateTime.of(LocalDate.now(), LocalTime.now())));
		repository5scores.save(new Score(player5, game3, 0,LocalDateTime.of(LocalDate.now(), LocalTime.now())));

	};
}
}







