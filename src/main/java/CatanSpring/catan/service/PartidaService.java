package CatanSpring.catan.service;

import CatanSpring.catan.model.Aresta;
import CatanSpring.catan.model.Assentament;
import CatanSpring.catan.model.CartaDesenvolupament;
import CatanSpring.catan.model.Ciutat;
import CatanSpring.catan.model.Hexagon;
import CatanSpring.catan.model.HexagonVertex;
import CatanSpring.catan.model.Jugador;
import CatanSpring.catan.model.Partida;
import CatanSpring.catan.model.RecursJugador;
import CatanSpring.catan.model.Vertex;
import CatanSpring.catan.dto.PartidaResponse;
import CatanSpring.catan.repository.PartidaRepository;
import CatanSpring.model.enums.ColorJugador;
import CatanSpring.model.enums.EstatPartida;
import CatanSpring.model.enums.TipusCarta;
import CatanSpring.model.enums.TipusRecurs;
import CatanSpring.model.enums.TipusTerreny;
import CatanSpring.model.gestioFitxers.ImportarFitxer;
import CatanSpring.catan.dto.TaulerHexagonResponse;
import CatanSpring.catan.repository.HexagonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


@Service
public class PartidaService {

	@Autowired 
	private PartidaRepository repo;

	private void provocarErrorRollback() {
	    throw new RuntimeException("Error simulat durant la inicialització");
	}
	@Transactional
	public ResponseEntity<PartidaResponse> iniciarPartida(Set<String> nomsJugadors, int puntsPerGuanyar) {
		
		if(nomsJugadors.size() < 2 || nomsJugadors.size()>4 ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Info-Catan", "De 2 a 4 jugadors maxim").build();
		}
			
		if(puntsPerGuanyar <= 0 || puntsPerGuanyar > 100) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Info-Catan", "De 1 a 100 punts maxim").build();
		}
		
		EstatPartida estat=EstatPartida.PREPARACIO;
		LocalDate dataInici= LocalDate.now();
		int ronda=1;
		int ultimaTirada=0;

		boolean dausTirats=false;
		boolean lladrePendent=false;
		Partida partida = new Partida(estat,dataInici,ronda,ultimaTirada,puntsPerGuanyar,dausTirats,lladrePendent);
		//assignar taulell a la partida
		crearTaulell(partida);

		provocarErrorRollback();
		//Barrejar noms, assignar colors i ordre
		prepararJugadors(nomsJugadors,partida);
		prepararCartes(partida);

		// Guardar la partida y todas sus relaciones
		Partida partidaGuardada = repo.saveAndFlush(partida);

		return ResponseEntity.status(HttpStatus.CREATED).header("Info-Catan", "Creació partida ID: " + partidaGuardada.getId()).body(PartidaResponse.de(partidaGuardada));
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
	
	public void prepararJugadors(Set<String> nomsJugadors, Partida partida) {
		List<String> jugadorsBarrejats = new ArrayList<>(nomsJugadors);
		Collections.shuffle(jugadorsBarrejats);
		int ordreJugador=0;
		for (String nom:jugadorsBarrejats){
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

	@Autowired
	private HexagonRepository hexagonRepository;
	public ResponseEntity<List<TaulerHexagonResponse>> mostrarTauler(Integer idPartida) {
		// Busca la partida a partir de l'ID
		Partida partida = repo.findById(idPartida).orElse(null);
		
		// Si no la troba, doncs, et llancará l'error 404.
		if (partida == null) {
	        return ResponseEntity.notFound().build();
	    }
		
		// Obté els hexagons d'aquella partida a partir de la consulta del repositori
		 List<Hexagon> hexagons = hexagonRepository.mostrarTauler(idPartida);
		 
		 // Guardem el ID del hexàgon on es troba el lladre (per defecte entenem que es DESERT)
		 Integer idLladre = partida.getLladre().getId();
		 
		 //Prepara la llista dels DTO de TaulerHexagonResponse que es retornaran. (En forma de JSON como se ve en el swagger gente)
		 List<TaulerHexagonResponse> resposta = new ArrayList<>();
		 
		 //Converteix cada hexàgon (adaptat a el demanat a TaulerHexagonResponse) i indica si conté el lladre.
		 for (Hexagon h : hexagons) {
		        boolean teLladre = h.getId().equals(idLladre);
		        resposta.add(TaulerHexagonResponse.de(h, teLladre));
		    }
		 
		 //Retorna el tauler de forma correcta (code 200)
		 return ResponseEntity.ok(resposta);
		
		 
	}

	@Transactional
	public ResponseEntity<PartidaResponse> tirarDaus(Integer idPartida) {
	    
		Partida partida = repo.findById(idPartida).orElse(null);

		//Si la partida no existeix, retorna un error 404.
	    if (partida == null) {
	        return ResponseEntity.notFound().header("Info-catan", "La partida no existeix").build();
	    }

	    //Si la partida no es troba en curs, aquesta llança un error 400
	    if (partida.getEstatPartida() != EstatPartida.EN_CURS) {
	        return ResponseEntity.badRequest().header("Info-Catan", "La partida no es troba en curs").build();
	    }

	    //Si ja s'han llançat els daus a la partida, llança també un error 404.
	    if (partida.isDausTirats()) {
	        return ResponseEntity.badRequest().header("Info-Catan", "Ja s'han llançat els daus").build();
	    }
	    
	    
	    
	    // Generem els valors dels 2 daus (de forma aleatória) i sumem el resultat
	    Random aleatori = new Random();

	    int dau1 = aleatori.nextInt(6) + 1;
	    int dau2 = aleatori.nextInt(6) + 1;
	    int tirada = dau1 + dau2;

	    //Es guarda el resultat dels daus i s'indica que ja s'han llançat els daus.
	    partida.setUltimaTirada(tirada);
	    partida.setDausTirats(true);
	    
	    // Si sortis el nombre 7, no es podria generar recursos.
	    if(tirada != 7) {
	    	for(Hexagon h: partida.getHexagons()) {
	    		
	    		//Només s'agafen els hexàgons que tenen el mateix numero que el de tirada.
	    		if(h.getNumero() != null && h.getNumero() == tirada) {
	    			
	    			//Es revisa els vèrtexs per a veure si hi trobem asentaments 
	    			for(HexagonVertex hv : h.getVertexsHexagon() ) {
	    				
	    				Assentament assentament = hv.getVertex().getAssentament();
	    				
	    				
	    				//Si no en troba, no es produeix absolutament res.
	    				if(assentament == null) {
	    					continue;
	    				}
	    				
	    				//Amb aquesta variable, podem associar el tipus de recurs depenent el terreny.
	    				TipusRecurs tipusRecurs = null;
	    				
	    				if(h.getTerreny() == TipusTerreny.BOSC) {
	    					tipusRecurs = TipusRecurs.FUSTA;
	    				} else if(h.getTerreny() == TipusTerreny.TURONS) {
	    					tipusRecurs = TipusRecurs.MAO;
	    				} else if (h.getTerreny() == TipusTerreny.PASTURES) {
	    					tipusRecurs = TipusRecurs.LLANA;
	    				} else if (h.getTerreny() == TipusTerreny.CAMPS) {
	    					tipusRecurs = TipusRecurs.BLAT;
	    				} else if (h.getTerreny() == TipusTerreny.MUNTANYES) {
	    					tipusRecurs = TipusRecurs.MINERAL;
	    				}
	    				
	    				//Si el terreny produeix algun recurs (corresponent), doncs s'inserta en el seu inventari. 
	    				//(Amb aixó, es controla el cas de DESERT, que no genera cap recurs)
	    				if(tipusRecurs != null) {
	    					Jugador j = assentament.getJugador();
	    					RecursJugador recurs = j.getInventari().get(tipusRecurs);
	    					
	    					// Un poblat rep una unitat. Les ciutats, dues
	    					
	    					int quantitat = 1;

	    					if (assentament instanceof Ciutat) {
	    					    quantitat = 2;
	    					}

	    					recurs.setQuantitat(recurs.getQuantitat() + quantitat);
	    				}
	    				
	    			}
	    			
	    		}
	    	}
	    
	    
	    }
	    
	    // Retornem les dades actualitzades de la partida.
	    return ResponseEntity.ok(PartidaResponse.de(partida));

	}
	
	// LEED EL COMENTARIO --> Probablemente si lo probais en el swagger, os dará 400, (BAD REQUEST).
	
	// Forzad a que la partida esté en curso (EN_CURS) (Hacedlo en MYSQL Workbench o el DBeaver para verlo). 
	// Además, comprovad que dausTirats es false, basicamente porque si ya habeis "tirado", este pasara a true y 
	// os volverá a saltar BAD_REQUEST, no porque este mal si no porque no puedes tirar más de 2 veces.
	
	// ------------------------------------------------------------------------------------------------------------
	
	// UPDATE partida
	// SET estat_partida = 'EN_CURS', daus_tirats = FALSE
	// WHERE id = 1;
	
	//Si ejecutais después de esto, os debería de salir 200 (OK). Cuando modifiqueis la base de datos, acordaos de volver a ejecutar el CatanApplication 
	
	// TODAVIA NO ESTA ACABADO porque se tiene que implementar lo de las construcciones y lo de pasar turno. 
	
	
	
}

	//public PartidaService(PartidaRepository repo) {
	//	this.repo = repo;
	// } --> Una alternativa



	//public List</*Classe*/> lista(){
	//	return repo.findAll();
	//}


	//public Optional</*nom_classe*/> buscar(Long id){
	//	return repo.findById(id);
		//}
	

