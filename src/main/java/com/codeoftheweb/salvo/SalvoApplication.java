package com.codeoftheweb.salvo;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
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
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();

	}

		@Bean
		public CommandLineRunner initData(PlayerRepository repository, GameRepository repository1, GamePlayerRepository repository2, ShipRepository repository3ship, SalvoRepository repository4salvo, ScoreRepository repository5scores) {
			return (args) -> {
				//games
				Game game1 = repository1.save(new Game(LocalDateTime.of(LocalDate.now(), LocalTime.now())));
				Game game2 = repository1.save(new Game(LocalDateTime.of(LocalDate.now(), LocalTime.now().plusHours(1))));
				Game game3 = repository1.save(new Game(LocalDateTime.of(LocalDate.now(), LocalTime.now().plusHours(2))));

//Players//Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
				Player player1 = repository.save(new Player("j.bauer@ctu.gov",passwordEncoder().encode("24")));
				Player player2 = repository.save(new Player("c.obrian@ctu.gov", passwordEncoder().encode("42")));
				Player player3 = repository.save(new Player("t.almeida@ctu.gov", passwordEncoder().encode("kb")));
				Player player4 = repository.save(new Player("j.bauer2@ctu.gov", passwordEncoder().encode("mole")));
				Player player5 = repository.save(new Player("d.palmer@whitehouse.gov", passwordEncoder().encode("1234")));

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
				repository3ship.save(new Ship("Destroyer", List.of("H2", "H3", "H4"), gamePlayer1));
				repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"), gamePlayer1));
				repository3ship.save(new Ship("Carrier", List.of("B2", "B3", "B4", "B5", "B6"), gamePlayer1));
				repository3ship.save(new Ship("Patrol", List.of("E2", "F2"), gamePlayer1));
				repository3ship.save(new Ship("BattleShip", List.of("A7", "B7", "C7", "D7"), gamePlayer1));
//game1 ships player2
				repository3ship.save(new Ship("Destroyer", List.of("H2", "H3", "H4"), gamePlayer2));
				repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"), gamePlayer2));
				repository3ship.save(new Ship("Carrier", List.of("B2", "B3", "B4", "B5", "B6"), gamePlayer2));
				repository3ship.save(new Ship("Patrol", List.of("E2", "F2"), gamePlayer2));
				repository3ship.save(new Ship("BattleShip", List.of("A7", "B7", "C7", "D7"), gamePlayer2));

//game1Salvoes
				repository4salvo.save(new Salvo(1, List.of("H2", "H3", "H4", "B3", "B8"), gamePlayer1));
				repository4salvo.save(new Salvo(1, List.of("E2", "B3", "J4", "A3", "D8"), gamePlayer2));

//score 1
				repository5scores.save(new Score(player1, game1, 0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
				repository5scores.save(new Score(player2, game1, 1, LocalDateTime.of(LocalDate.now(), LocalTime.now())));

				//**game 2**//

//game2 ships player3
				repository3ship.save(new Ship("Destroyer", List.of("H2", "I2", "J2"), gamePlayer3));
				repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"), gamePlayer3));
				repository3ship.save(new Ship("Carrier", List.of("C2", "C3", "C4", "C5", "C6"), gamePlayer3));
				repository3ship.save(new Ship("Patrol", List.of("E9", "E10"), gamePlayer3));
				repository3ship.save(new Ship("BattleShip", List.of("E10", "F10", "G10", "H10"), gamePlayer3));
//game2 ships player4
				repository3ship.save(new Ship("Destroyer", List.of("j2", "K2", "L2"), gamePlayer4));
				repository3ship.save(new Ship("Submarine", List.of("J4", "J5", "J6"), gamePlayer4));
				repository3ship.save(new Ship("Carrier", List.of("C2", "C3", "C4", "C5", "C6"), gamePlayer4));
				repository3ship.save(new Ship("Patrol", List.of("H9", "H10"), gamePlayer4));
				repository3ship.save(new Ship("BattleShip", List.of("B10", "C10", "D10", "E10"), gamePlayer4));

//game2Salvoes
				repository4salvo.save(new Salvo(1, List.of("C2", "I3", "D4", "F3", "E8"), gamePlayer3));
				repository4salvo.save(new Salvo(1, List.of("B2", "D8", "G4", "F10", "C3"), gamePlayer4));

//score game2
				repository5scores.save(new Score(player3, game2, 0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
				repository5scores.save(new Score(player4, game2, 1, LocalDateTime.of(LocalDate.now(), LocalTime.now())));

				//**game 3**//

//game3 ships player3
				repository3ship.save(new Ship("Destroyer", List.of("j2", "K2", "L2"), gamePlayer5));
				repository3ship.save(new Ship("Submarine", List.of("J4", "J5", "J6"), gamePlayer5));
				repository3ship.save(new Ship("Carrier", List.of("C2", "C3", "C4", "C5", "C6"), gamePlayer5));
				repository3ship.save(new Ship("Patrol", List.of("H9", "H10"), gamePlayer5));
				repository3ship.save(new Ship("BattleShip", List.of("B10", "C10", "D10", "E10"), gamePlayer5));
//game3 ships player 4
				repository3ship.save(new Ship("Destroyer", List.of("H2", "H3", "H4"), gamePlayer6));
				repository3ship.save(new Ship("Submarine", List.of("A4", "A5", "A6"), gamePlayer6));
				repository3ship.save(new Ship("Carrier", List.of("B2", "B3", "B4", "B5", "B6"), gamePlayer6));
				repository3ship.save(new Ship("Patrol", List.of("E2", "F2"), gamePlayer6));
				repository3ship.save(new Ship("BattleShip", List.of("A7", "B7", "C7", "D7"), gamePlayer6));

//game3Salvos
				repository4salvo.save(new Salvo(1, List.of("H2", "H3", "H4", "B3", "B8"), gamePlayer5));
				repository4salvo.save(new Salvo(1, List.of("E2", "B3", "J4", "A3", "D8"), gamePlayer6));

//score game3
				repository5scores.save(new Score(player2, game3, 1, LocalDateTime.of(LocalDate.now(), LocalTime.now())));
				repository5scores.save(new Score(player5, game3, 0, LocalDateTime.of(LocalDate.now(), LocalTime.now())));

			};
		}
	}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userName -> {
			Player player = playerRepository.findByUserName(userName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + userName);
			}
		});//toma el usuario que ha ingresado para iniciar sesion, buscar en la base de datos con ese nombre
		   //y devolver un objeto UserDetails con nombre, pass, permiso, etc.
	}

}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				//aca elegimos que paginas son publicas y cuales no
				.antMatchers( "/web/**","/api/games","/api/players","/rest/**").permitAll()
				.anyRequest().authenticated()
				.and();
		//metodos de configuracion que reemplazan and() y formLogin()
		http.formLogin()
				.usernameParameter("name") //
				.passwordParameter("pwd") //
				.loginPage("/api/login");//

		http.logout().logoutUrl("/api/logout");

		// deshabilita la comprobacion de CSRF tokens
		http.csrf().disable();

		// si el usuario no esta autorizado, enviar un fallo de autenticacion
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// si el login es exitoso limpiar los fallos de autenticacion
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		//si el login falla, enviar un fallo de autenticacion
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// si el logout es exitoso, enviar una respuesta exitosa
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

}






