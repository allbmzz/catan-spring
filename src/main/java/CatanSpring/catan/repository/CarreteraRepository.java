package CatanSpring.catan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import CatanSpring.catan.model.Carretera;

public interface CarreteraRepository extends JpaRepository<Carretera, Long> {
	
	@Query(name = "Carretera.numCarreteresJugador")  // reutilitzem la @NamedQuery creada a Hibernate
	Long numCarreteresJugador(@Param("id") Long idJugador);
}
