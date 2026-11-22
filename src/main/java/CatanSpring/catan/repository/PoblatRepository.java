package CatanSpring.catan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import CatanSpring.catan.model.Poblat;

public interface PoblatRepository extends JpaRepository<Poblat, Long> {
	
	@Query(name = "Poblat.numPoblatsJugador")
	Long numPoblatsJugador(@Param("id") Integer idJugador);
}
