package com.codeoftheweb.salvo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource//CONVIERTE GAMEREPOSITORY EN UN REST REPOSITORY


public interface GameRepository extends JpaRepository<Game, Long> {


}//JPA
//administran la base de datos
//repositorio JPA, gestiona las instancias de Game. es una extension de crud repository (create,read, update, delete)
//incluyen el Count, delete, findAll, findById, save, salAll.
//Los detalles del codigo los podemos chequear en el H2.


//@RepositoryRestResource//
//REST REPOSITORY
//FACILITA ENVIAR INSTANCIAS DE CLASES EN ANOTACION JSON,
// REPOSITORY REST RESOURCE=RUTAS,


