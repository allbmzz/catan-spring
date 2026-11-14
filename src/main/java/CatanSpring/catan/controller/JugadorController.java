
package CatanSpring.catan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import CatanSpring.catan.dto.JugadorResponse;
import CatanSpring.catan.service.JugadorService;

@RestController
@RequestMapping("/jugador") // Ruta base exigida per l'enunciat REVISAR RUTA
public class JugadorController {

	private final JugadorService serv;

	// Amb un sol constructor, no cal @Autowired
	public JugadorController(JugadorService serv) {
		this.serv = serv;
	}

	@GetMapping("/{idJugador}/recursos")
	public ResponseEntity<JugadorResponse> llistarRecursos(
			@PathVariable Long idJugador) {

		// Si existeix retorna 200 OK; si no, retorna 404 NOT FOUND
		return serv.llistarRecursos(idJugador).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}
