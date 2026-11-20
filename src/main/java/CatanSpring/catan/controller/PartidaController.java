package CatanSpring.catan.controller;

import CatanSpring.catan.dto.JugadorRequest.PartidaRequest;
import CatanSpring.catan.dto.PartidaResponse;
import CatanSpring.catan.dto.TaulerHexagonResponse;
import CatanSpring.catan.service.PartidaService;
import CatanSpring.catan.model.*;
import CatanSpring.model.enums.*;
import CatanSpring.model.gestioFitxers.ImportarFitxer;
import CatanSpring.catan.dto.TaulerHexagonResponse;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
	//ResponseEntity<?> està amb ? per evitar que mostri un element partida sense dades al retornar badRequest
	@PostMapping
	public ResponseEntity<?> iniciarPartida(@RequestParam Set<String> nomsJugadors, @RequestParam int puntsPerGuanyar) {

		return partidaService.iniciarPartida(nomsJugadors, puntsPerGuanyar);
	}
	
	@GetMapping("/{idPartida}/tauler")
	public ResponseEntity<List<TaulerHexagonResponse>> mostrarTauler(
	        @PathVariable Integer idPartida) {
	    return partidaService.mostrarTauler(idPartida);
	}
	
	
}
