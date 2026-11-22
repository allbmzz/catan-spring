package CatanSpring.catan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import CatanSpring.catan.model.Ciutat;

public interface CiutatRepository extends JpaRepository<Ciutat, Long> {
	
	@Query(name = "Ciutat.numCiutatsJugador")
	Long numCiutatsJugador(@Param("id") Integer idJugador);
}
