package CatanSpring.catan.controller;

import CatanSpring.catan.dto.JugadorRequest.PartidaRequest;
import CatanSpring.catan.dto.PartidaResponse;
import CatanSpring.catan.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/jugador")
public class PartidaController {
	
	@Autowired
	private PartidaService partidaService;
	
	
	//Constructor. Injectará automàticament la instància a partidaService.
	public PartidaController(PartidaService partidaService) {
		
		this.partidaService = partidaService;
		
	}
	
	
	// L'endpoint per a poder realitzar una nova partida.
	@PostMapping
	public ResponseEntity<PartidaResponse> crearPartida(@RequestBody PartidaRequest request){
		// Deleguem tota la lògica de negoci i la transacció de BD a la capa de servei --> Per aixó utilitzem el mètode iniciarPartida, allotjat en PartidaService
		PartidaResponse response = partidaService.iniciarPartida(request);
		// Retornem el codi 201 Created juntament amb el cos de la resposta (PartidaResponse)
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	

}
