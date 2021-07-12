package com.codeoftheweb.salvo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Date;
import java.util.Optional;

@RepositoryRestResource



public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUserName(@Param("name") String name);
}

