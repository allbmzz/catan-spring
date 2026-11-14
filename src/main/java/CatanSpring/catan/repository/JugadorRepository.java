package CatanSpring.catan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import CatanSpring.catan.model.*;

//Cambiar el Jugador repository por classe en CatanSpring.models

//NO PONER ETIQUETA, YA HEREDA DE JPAREPOSITORY
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
	
	 @Query(name = "Jugador.buscarRecursos")  // reutilizem la namedQuery creada a Hibernate i simplement la cridem al metode
	 List<RecursJugador> llistarRecursos(@Param("id") int idJugador);
}
