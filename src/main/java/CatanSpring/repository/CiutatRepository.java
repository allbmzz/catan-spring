package CatanSpring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

//Cambiar el Jugador repository por classe en CatanSpring.models

//NO PONER ETIQUETA, YA HEREDA DE JPAREPOSITORY
public interface CiutatRepository extends JpaRepository<CiutatRepository, Long> {
	
	
	

}
