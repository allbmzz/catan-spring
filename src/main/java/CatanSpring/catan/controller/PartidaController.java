package CatanSpring.catan.controller;

import CatanSpring.catan.dto.PartidaResponse;
import CatanSpring.catan.dto.TaulerHexagonResponse;
import CatanSpring.catan.dto.TiradaDausResponse;
import CatanSpring.catan.service.PartidaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/partida")
public class PartidaController {
	
	@Autowired
	private PartidaService partidaService;
	
	//Constructor. Injectarà automàticament la instància a partidaService.
	public PartidaController(PartidaService partidaService) {
		this.partidaService = partidaService;
	}
	
	@PostMapping
	public ResponseEntity<PartidaResponse> iniciarPartida(@RequestParam Set<String> nomsJugadors, @RequestParam int puntsPerGuanyar) {
		return partidaService.iniciarPartida(nomsJugadors, puntsPerGuanyar);
	}
	
	@GetMapping("/{idPartida}/tauler")
	public ResponseEntity<List<TaulerHexagonResponse>> mostrarTauler(@PathVariable Integer idPartida) {
	    return partidaService.mostrarTauler(idPartida);
	}
	
	@PostMapping("/{idPartida}/daus")
	public ResponseEntity<TiradaDausResponse> tirarDaus(@PathVariable Integer idPartida) {
	    return partidaService.tirarDaus(idPartida);
	}
}
