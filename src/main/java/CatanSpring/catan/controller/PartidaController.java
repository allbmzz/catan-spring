package CatanSpring.catan.controller;

import CatanSpring.catan.dto.JugadorRequest.PartidaRequest;
import CatanSpring.catan.dto.PartidaResponse;
import CatanSpring.catan.service.PartidaService;
import CatanSpring.catan.model.*;
import CatanSpring.model.enums.*;
import CatanSpring.model.gestioFitxers.ImportarFitxer;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/partida")
public class PartidaController {
	
	@Autowired
	private PartidaService partidaService;
	
	
	//Constructor. Injectarà automàticament la instància a partidaService.
	public PartidaController(PartidaService partidaService) {
		
		this.partidaService = partidaService;
		
	}
	
	public void IniciarPartida(Session session, List<String> nomsJugadors, int puntsPerGuanyar) {
		
	EstatPartida estat=EstatPartida.PREPARACIO;
	LocalDate dataInici= LocalDate.now();
	int ronda=0;
	int ultimaTirada=0;
	//System.out.print("Introdueix la quantitat de punts per guanyar: ");
	//int puntsPerGuanyar=getInt(1,100);
	boolean dausTirats=false;
	boolean lladrePendent=false;
	Partida partida = new Partida(estat,dataInici,ronda,ultimaTirada,puntsPerGuanyar,dausTirats,lladrePendent);
	//assignar taulell a la partida
	crearTaulell(partida);

	//Barrejar noms, assignar colors i ordre
	prepararJugadors(nomsJugadors,partida);
	
	prepararCartes(partida);

	
	}
	
	public void prepararCartes(Partida partida) {
		List<CartaDesenvolupament> pila = new ArrayList<>();
		
		// Bucle per a les cartes de CAVALLERS
		for(int i = 0; i < 14; i++) {
			CartaDesenvolupament cavallers = new CartaDesenvolupament();
			cavallers.setTipus(TipusCarta.CAVALLER);
			cavallers.setPartida(partida);
			pila.add(cavallers);
		}
		
		// Bucle per a les cartes de PUNT_VICTORIA
		for(int i = 0; i < 5; i++) {
			CartaDesenvolupament puntVictoria = new CartaDesenvolupament();
			puntVictoria.setTipus(TipusCarta.PUNT_VICTORIA);
			puntVictoria.setPartida(partida);
			pila.add(puntVictoria);
		}
		
		// Bucle per a les cartes de MONOPOLI
		for(int i = 0; i < 2; i++) {
			CartaDesenvolupament monopoli = new CartaDesenvolupament();
			monopoli.setTipus(TipusCarta.MONOPOLI);
			monopoli.setPartida(partida);
			pila.add(monopoli);
		}
		
		//Barrejem la pila per a que les compres siguin random
		Collections.shuffle(pila);
		
		partida.setPilaCartes(pila);
	}
	
	public void prepararJugadors(List<String> nomsJugadors, Partida partida) {
		Collections.shuffle(nomsJugadors);
		int ordreJugador=0;
		for (String nom:nomsJugadors){
			ColorJugador color = ColorJugador.values()[ordreJugador];
			ordreJugador++;
			Jugador jugador=new Jugador(nom,color,ordreJugador);
			jugador.setPartida_id(partida);
			partida.getJugadors().add(jugador);
		}
		for (Jugador j:partida.getJugadors()){
			Map<TipusRecurs, RecursJugador> inventari=j.getInventari();
			for(TipusRecurs tr:TipusRecurs.values()){
				RecursJugador rj=new RecursJugador(tr,0);
				rj.setJugador(j);
				inventari.put(tr,rj);
			}
		}
	}
	
	
	private HashSet<HexagonVertex> carregarDades(Partida partida) {
		
		HashMap<Integer, Vertex> mapaVertex=new HashMap<>();
		HashMap<Integer, Hexagon> mapaHexagons=new HashMap<>();
		partida.setVertexs(ImportarFitxer.importar("vertexs.csv",mapaVertex,mapaHexagons));
		partida.setHexagons(ImportarFitxer.importar("hexagons.csv",mapaVertex,mapaHexagons));
		HashSet <HexagonVertex> hexagonsVertex=ImportarFitxer.importar("hexagon_vertex.csv",mapaVertex,mapaHexagons);
		partida.setArestes(ImportarFitxer.importar("arestes.csv",mapaVertex,mapaHexagons));
		
		return hexagonsVertex;
	}
	
	
	public void crearTaulell(Partida partida) {
		
		HashSet<HexagonVertex> hexagonsVertex = carregarDades(partida);
		
		ArrayList<Integer> numerosHexagons = new ArrayList<>(List.of(2,3,3,4,4,5,5,6,6,8,8,9,9,10,10,11,11,12));
		ArrayList<TipusTerreny> terrenyHexagon = new ArrayList<>(List.of(TipusTerreny.BOSC, TipusTerreny.BOSC, TipusTerreny.BOSC, TipusTerreny.BOSC,
				TipusTerreny.PASTURES, TipusTerreny.PASTURES, TipusTerreny.PASTURES, TipusTerreny.PASTURES,
				TipusTerreny.CAMPS, TipusTerreny.CAMPS, TipusTerreny.CAMPS, TipusTerreny.CAMPS,
				TipusTerreny.TURONS, TipusTerreny.TURONS, TipusTerreny.TURONS,
				TipusTerreny.MUNTANYES, TipusTerreny.MUNTANYES, TipusTerreny.MUNTANYES,
				TipusTerreny.DESERT));

		Collections.shuffle(numerosHexagons);
		Collections.shuffle(terrenyHexagon);
		int hex = 0;
		int num = 0;
		for (Hexagon h : partida.getHexagons()) {
		    h.setPartida_id(partida);
		    TipusTerreny terreny = terrenyHexagon.get(hex);
		    h.setTerreny(terreny);
		    if (terreny == TipusTerreny.DESERT) {
		    	partida.setLladre(h);
		        h.setNumero(null);
		    } else {
		        h.setNumero(numerosHexagons.get(num));
		        num++;
		    }
		    hex++;
		    
		    for(HexagonVertex hv : hexagonsVertex) {
		    	if (hv.getHexagon().getCodi() == h.getCodi()) {
		    		h.getVertexsHexagon().add(hv);
		    		hv.setHexagon(h);
		    	}
		    }
		}
		for (Vertex v:partida.getVertexs()){
			v.setPartida_id(partida);
			for(HexagonVertex hv : hexagonsVertex) {
		    	if (hv.getVertex().getCodi() == v.getCodi()) {
		    		v.getHexagonVertex().add(hv);
		    		hv.setVertex(v);
		    	}
		    }
		}
		for (Aresta a:partida.getArestes()){
			a.setPartida_id(partida);
		}
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
