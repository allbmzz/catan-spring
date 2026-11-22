
package CatanSpring.catan.controller;

import CatanSpring.catan.dto.ConstruccioRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import CatanSpring.catan.dto.JugadorResponse;
import CatanSpring.catan.service.JugadorService;

@RestController
@RequestMapping("/jugador") // Ruta base exigida per l'enunciat
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
		return serv.mostrarRecursos(idJugador).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/{idJugador}/passar")
	public ResponseEntity<String> passarTorn(@PathVariable Long idJugador/*, @RequestBody ConstruccioRequest request*/) {	
		return serv.passarTorn(idJugador);	
	}

	@PostMapping("/{idJugador}/construir")
	public ResponseEntity<String> construir(@PathVariable Long idJugador, @RequestBody ConstruccioRequest request) {
		return serv.construir(idJugador, request);
	}
}
