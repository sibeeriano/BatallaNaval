package com.codeoftheweb.salvo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Date;

@RepositoryRestResource



public interface PlayerRepository extends JpaRepository<Player, Long> {
 //List<Player> findByCreationDate(Date date);

}

