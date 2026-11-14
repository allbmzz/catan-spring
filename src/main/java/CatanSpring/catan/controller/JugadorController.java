package CatanSpring.catan.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import CatanSpring.catan.model.RecursJugador;
import CatanSpring.catan.service.JugadorService;

@RestController
@RequestMapping("/api/jugador")
public class JugadorController {
	
	// Amb un sol constructor, no cal @Autowired
	private final JugadorService serv;
	
	public JugadorController(JugadorService serv) {
		this.serv = serv;
	}
	

	@GetMapping("/{id}/recursos")
	public List<RecursJugador> llistarRecursos(@PathVariable("id") int idJugador){
		return serv.llistarRecursos(idJugador);
	}
	
	@GetMapping("/prova")
	public String prova() {
	    return "El controller funciona!";
	}
	
	//@GetMapping("/{id}")
	//public ResponseEntity<?> buscar(@PathVariable Long id){
	//	Optional</*nom_classe*/> guardado = serv.buscar(id);
		
	//	if(guardado.isEmpty()) {
	//		return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Jugador", "Jugador NO TROBAT").body("");
	//	}
		
	//	return ResponseEntity.ok(guardado.get());
	//}
	
}
