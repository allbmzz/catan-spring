package CatanSpring.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import CatanSpring.service.JugadorService;

@RestController
@RequestMapping("/api/jugador")
public class JugadorController {
	
	@Autowired
	private JugadorService serv;
	
	
	@GetMapping
	public List</*nom_classe*/> listar(){
		return serv.lista();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id){
		Optional</*nom_classe*/> guardado = serv.buscar(id);
		
		if(guardado.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Jugador", "Jugador NO TROBAT").body("");
		}
		
		return ResponseEntity.ok(guardado.get());
	}
	
}
