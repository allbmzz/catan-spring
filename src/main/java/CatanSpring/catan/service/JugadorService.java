
package CatanSpring.catan.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CatanSpring.catan.dto.JugadorResponse;
import CatanSpring.catan.repository.JugadorRepository;

@Service
public class JugadorService {

	// Injecció per constructor: no cal @Autowired si només hi ha un constructor
	private final JugadorRepository repo;

	public JugadorService(JugadorRepository repo) {
		this.repo = repo;
	}

	@Transactional(readOnly = true)
	public Optional<JugadorResponse> mostrarRecursos(Long idJugador) {
		// Busquem el jugador i el transformem a DTO si existeix
		// Així evitem retornar directament l'entitat JPA
		return repo.findById(idJugador).map(JugadorResponse::de);
	}
}
