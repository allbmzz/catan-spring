package CatanSpring.catan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import CatanSpring.catan.model.RecursJugador;

//Cambiar el Jugador repository por classe en CatanSpring.models

//NO PONER ETIQUETA, YA HEREDA DE JPAREPOSITORY
public interface RecursJugadorRepository extends JpaRepository<RecursJugador, Long> {
	
	
	

}
