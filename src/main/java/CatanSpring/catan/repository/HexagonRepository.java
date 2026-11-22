package CatanSpring.catan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import CatanSpring.catan.model.Hexagon;

public interface HexagonRepository extends JpaRepository<Hexagon, Long> {
	
	// reutilizem la namedQuery creada a Hibernate i simplement la cridem al metode
	@Query(name = "Hexagon.MostrarTauler")
	List<Hexagon> mostrarTauler(@Param("idPartida") Integer idPartida);
}
