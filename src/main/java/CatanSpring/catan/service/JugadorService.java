
package CatanSpring.catan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CatanSpring.catan.dto.ConstruccioRequest;
import CatanSpring.catan.dto.JugadorResponse;
import CatanSpring.catan.repository.*;
import CatanSpring.model.enums.*;
import CatanSpring.catan.model.*;

@Service
public class JugadorService {

	// Injecció per constructor: no cal @Autowired si només hi ha un constructor
	private final JugadorRepository repo;

	public JugadorService(JugadorRepository repo) {
		this.repo = repo;
	}
	
	// com que nomes podem especificar un constructor, la resta de repositoris que fem servir seran amb @Autowired
	@Autowired
	private ArestaRepository arestaRepo;
	
	@Autowired
	private CarreteraRepository carreteraRepo;
	
	@Autowired
	private VertexRepository vertexRepo;

	@Autowired
	private PoblatRepository poblatRepo;
	
	@Autowired
	private CiutatRepository ciutatRepo;
	
	@Transactional(readOnly = true)
	public Optional<JugadorResponse> mostrarRecursos(Long idJugador) {
		// Busquem el jugador i el transformem a DTO si existeix
		// Així evitem retornar directament l'entitat JPA
		return repo.findById(idJugador).map(JugadorResponse::de);
	}
	
	@Transactional
	public ResponseEntity<String> passarTorn(Long idJugador) {
		
		Jugador jugador = repo.findById(idJugador).orElse(null);
		
		if (jugador == null) { 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat el jugador de id " + idJugador);
		}
		Partida partida = jugador.getPartida_id();


		// Rebutjar no es troba la partida o la partida no conté al jugador (doble verificació)
		if (partida == null || !partida.getJugadors().contains(jugador)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha trobat la partida d'aquest jugador");

			//Rebutjar una partida ja finalitzada
		} else if (partida.getEstatPartida().equals(EstatPartida.FINALITZADA)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("La partida d'aquest jugador ja ha finalitzat");

		} else if (partida.getEstatPartida().equals(EstatPartida.EN_CURS)){

			// Comprovacions de seguretat. Si encara és el torn del jugador
			if (partida.getActiu() == null || !partida.getActiu().equals(jugador)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Encara no es el torn del jugador");
			}

			// Comprovacions de seguretat. Si no s'han tirat els daus prèviament
			if (!partida.isDausTirats()) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body( "Encara no s'han tirat els daus");
			}

			// Calcular el següent torn
			int seguentTorn = jugador.getOrdreTorn() + 1;

			// Quan hagin passat tots els jugadors, tornem al primer i incrementem la ronda
			if (seguentTorn > partida.getJugadors().size()) {
				seguentTorn = 1;
				partida.setRonda(partida.getRonda() + 1);
			}

			// Assignar el nou jugador actiu amb un bucle
			for (Jugador j : partida.getJugadors()) {
				if (j.getOrdreTorn() == seguentTorn) {
					partida.setActiu(j);
					break;
				}
			}

			// Reiniciar els daus per al següent jugador
			partida.setDausTirats(false);

		} else if (partida.getEstatPartida().equals(EstatPartida.PREPARACIO)) {

			// Rebutja si no és el torn del jugador
			if (partida.getActiu() == null || !partida.getActiu().equals(jugador)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Encara no es el torn del jugador");
			}

			// Comptar els poblats i les carreteres del jugador
			long poblats = jugador.getConstruccionsJugador().stream().filter(c -> c instanceof Poblat).count();
			long carreteres = jugador.getConstruccionsJugador().stream().filter(c -> c instanceof Carretera).count();

			// Rebutja si no s'han construït el poblat i la carretera en el torn de preparació
			if (partida.getRonda() == 1) {
				if (poblats < 1 || carreteres < 1) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body("Has de col·locar un poblat i una carretera abans de passar el torn");
				}

			} else if (partida.getRonda() == 2) {
				if (poblats < 2 || carreteres < 2) {
					return ResponseEntity.status(HttpStatus.CONFLICT).body("Has de col·locar un poblat i una carretera abans de passar el torn");
				}

			}

			// Gestionar el canvi de torn segons la ronda
			int seguentTorn;

			if (partida.getRonda() == 1) {
				seguentTorn = jugador.getOrdreTorn() + 1;

				// Quan acaba el primer ordre de torn, començar la segona ronda
				if (seguentTorn > partida.getJugadors().size()) {
					seguentTorn = partida.getJugadors().size();
					partida.setRonda(2);
				}

			} else {
				seguentTorn = jugador.getOrdreTorn() - 1;
			}

			// Comprovar si tots els jugadors han acabat les dues rondes
			if (partida.getRonda() == 2 && seguentTorn < 1) {

				// Buscar el primer jugador per començar la partida
				for (Jugador j : partida.getJugadors()) {
					if (j.getOrdreTorn() == 1) {
						partida.setActiu(j);
						break;
					}
				}

				// Finalitzar la preparació
				partida.setEstatPartida(EstatPartida.EN_CURS);
				partida.setRonda(1);

			} else {

				// Assignar el següent jugador actiu
				for (Jugador j : partida.getJugadors()) {
					if (j.getOrdreTorn() == seguentTorn) {
						partida.setActiu(j);
						break;
					}
				}

			}
		}
		return ResponseEntity.status(HttpStatus.OK).body("Torn passat");

	}

	@Transactional
	public ResponseEntity<String> construir(Long idJugador, ConstruccioRequest request) {
		
		Jugador jugador = repo.findById(idJugador).orElse(null);
		
		if (jugador == null) { 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jugador no trobat");
		}
		
		if (request == null || request.getTipus() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipus de construcció invàlida");
		}
		
		// deleguem el proces a diferents funcions segons el tipus de construccio escollida
		if (request.getTipus().toUpperCase().equals("CARRETERA")) {
			Carretera c = construirCarretera(jugador, request.getIdAresta());
			
			if (c == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error durant la creació de la carretera");
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).header("Catan-info", "Carretera construida correctament").body("CARRETERA CONSTRUIDA " + c);
			
		} else if (request.getTipus().toUpperCase().equals("POBLAT")) {
			Poblat p = construirPoblat(jugador, request.getIdVertex());
			
			if (p == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error durant la creació del poblat");
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).header("Catan-info", "Poblat construit correctament").body("POBLAT CONSTRUIT " + p);
			
		} else if (request.getTipus().toUpperCase().equals("CIUTAT")){
			Ciutat c = millorarACiutat(jugador, request.getIdVertex());
			
			if (c == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error durant la creació de la ciutat");
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).header("Catan-info", "Ciutat construïda correctament").body("CIUTAT CONSTRUIDA " + c);
			
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipus de construcció invàlida");
		}
	}
	
	private Carretera construirCarretera(Jugador j, Long idAresta) {
		
		if (j==null) return null;

		Aresta a = arestaRepo.findById(idAresta).orElse(null);
		
		if (a==null) return null;

		if(j.getPartida_id().getId()!=a.getPartida_id().getId()){
			System.out.println("L'aresta i el jugador no pertanyen a la mateixa partida");
			return null;
		}

		if (a.getCarretera()!=null) {
			System.out.println("Aquesta aresta ja té una carretera");
			return null;
		}

		// Comprovació del torn del jugador
		if(!j.getPartida_id().getActiu().equals(j)){
			System.out.println("No és el torn del jugador amb id: "+ j.getId());
			return null;
		}

		// Comprovació del límit de 15 carreteres per jugador
		Long numCarreteres = carreteraRepo.numCarreteresJugador((long) j.getId());  // fem cast a Long per que getId() retorna int

		if(numCarreteres>=15){
			System.out.println("El jugador ja té el màxim de 15 carreteres");
			return null;
		}

		// Verificació dels assentaments dels vertex que formen l'aresta
		Assentament ava=a.getVertexA().getAssentament();
		Assentament avb=a.getVertexB().getAssentament();

		boolean connectatA = false;
		boolean connectatB = false;

		// Comprovació del vertex A
		if(ava!=null){
			if(ava.getJugador().getId()==j.getId()) connectatA=true;
		}else{
			for(Aresta ar : a.getVertexA().getArestesA()){
				if(ar!=a && ar.getCarretera()!=null && ar.getCarretera().getJugador().getId()==j.getId()){
					connectatA=true;
					break;
				}
			}

			if(!connectatA){
				for(Aresta ar : a.getVertexA().getArestesB()){
					if(ar!=a && ar.getCarretera()!=null && ar.getCarretera().getJugador().getId()==j.getId()){
						connectatA=true;
						break;
					}
				}
			}
		}

		// Comprovació del vertex B
		if(avb!=null){
			if(avb.getJugador().getId()==j.getId()) connectatB=true;
		}else{
			for(Aresta ar : a.getVertexB().getArestesA()){
				if(ar!=a && ar.getCarretera()!=null && ar.getCarretera().getJugador().getId()==j.getId()){
					connectatB=true;
					break;
				}
			}

			if(!connectatB){
				for(Aresta ar : a.getVertexB().getArestesB()){
					if(ar!=a && ar.getCarretera()!=null && ar.getCarretera().getJugador().getId()==j.getId()){
						connectatB=true;
						break;
					}
				}
			}
		}

		// Comprovació de la connectivitat
		if(j.getPartida_id().getEstatPartida()!=EstatPartida.PREPARACIO){
			if(!connectatA && !connectatB){
				System.out.println("L'aresta no està connectada a cap carretera o assentament propi");
				return null;
			}
		}else{
			// Pendent: comprovar que un dels extrems sigui el poblat acabat de col·locar
		}

		// Comprovació dels recursos
		if(j.getPartida_id().getEstatPartida()!=EstatPartida.PREPARACIO){
			RecursJugador fusta = j.getInventari().get(TipusRecurs.FUSTA);
			RecursJugador argila = j.getInventari().get(TipusRecurs.MAO);

			if(fusta==null || argila==null || fusta.getQuantitat()<1 || argila.getQuantitat()<1){
				System.out.println("No tens recursos suficients per construir una carretera");
				return null;
			}

			fusta.setQuantitat(fusta.getQuantitat()-1);
			argila.setQuantitat(argila.getQuantitat()-1);
		}

		// Creació de la carretera
		Carretera c = new Carretera(a);
		c.setJugador(j);
		a.setCarretera(c);
		
		carreteraRepo.save(c);

		return c;
	}

	private Poblat construirPoblat(Jugador j, Long idVertex) {

		if (j==null) return null;
		
		Vertex v = vertexRepo.findById(idVertex).orElse(null);
		
		if (v==null) return null;

		if(j.getPartida_id().getId()!=v.getPartida_id().getId()){
			System.out.println("El vertex i el jugador no pertanyen a la mateixa partida");
			return null;
		}

		// Comprovació del torn del jugador
		if(!j.getPartida_id().getActiu().equals(j)){
			System.out.println("No és el torn del jugador amb id: "+j.getId());
			return null;
		}

		if(v.getAssentament()!=null){
			System.out.println("Aquest vertex ja té un assentament");
			return null;
		}

		// Comprovació de la regla de la distància
		// Pendent de comprovar els assentaments dels vertex adjacents

		// Comprovació de la connexió amb una carretera pròpia
		if(j.getPartida_id().getEstatPartida()!=EstatPartida.PREPARACIO){
			boolean connectat = false;

			for(Aresta ar : v.getArestesA()){
				if(ar.getCarretera()!=null && ar.getCarretera().getJugador().getId()==j.getId()){
					connectat=true;
					break;
				}
			}

			if(!connectat){
				for(Aresta ar : v.getArestesB()){
					if(ar.getCarretera()!=null && ar.getCarretera().getJugador().getId()==j.getId()){
						connectat=true;
						break;
					}
				}
			}

			if(!connectat){
				System.out.println("El vertex no està connectat a cap carretera pròpia");
				return null;
			}
		}

		// Comprovació del límit de 5 poblats
		Long numPoblats = poblatRepo.numPoblatsJugador(j.getId());

		if(numPoblats>=5){
			System.out.println("El jugador ja té el màxim de 5 poblats");
			return null;
		}

		// Comprovació dels recursos
		if(j.getPartida_id().getEstatPartida()!=EstatPartida.PREPARACIO){
			RecursJugador fusta = j.getInventari().get(TipusRecurs.FUSTA);
			RecursJugador argila = j.getInventari().get(TipusRecurs.MAO);
			RecursJugador blat = j.getInventari().get(TipusRecurs.BLAT);
			RecursJugador llana = j.getInventari().get(TipusRecurs.LLANA);

			if(fusta==null || argila==null || blat==null || llana==null ||
					fusta.getQuantitat()<1 || argila.getQuantitat()<1 ||
					blat.getQuantitat()<1 || llana.getQuantitat()<1){
				System.out.println("No tens recursos suficients per construir un poblat");
				return null;
			}

			fusta.setQuantitat(fusta.getQuantitat()-1);
			argila.setQuantitat(argila.getQuantitat()-1);
			blat.setQuantitat(blat.getQuantitat()-1);
			llana.setQuantitat(llana.getQuantitat()-1);
		}

		// Creació del poblat
		Poblat p = new Poblat(v);
		p.setJugador(j);
		v.setAssentament(p);

		// Repartiment dels recursos inicials del segon poblat
		// Pendent d'implementar segons les relacions entre vertex i terrenys

		poblatRepo.save(p);
		return p;
	}


	private Ciutat millorarACiutat(Jugador j, Long idVertex) {

		if (j==null) return null;

		Vertex v = vertexRepo.findById(idVertex).orElse(null);
		if (v==null) return null;

		if(j.getPartida_id().getId()!=v.getPartida_id().getId()){
			System.out.println("El vertex i el jugador no pertanyen a la mateixa partida");
			return null;
		}

		// Comprovació del torn del jugador
		if(!j.getPartida_id().getActiu().equals(j)){
			System.out.println("No és el torn del jugador amb id: "+j.getId());
			return null;
		}
		
		if(v.getAssentament()==null || !(v.getAssentament() instanceof Poblat)){
			System.out.println("Aquest vertex no té cap poblat per millorar");
			return null;
		}

		Poblat p = (Poblat) v.getAssentament();

		if(p.getJugador().getId()!=j.getId()){
			System.out.println("Aquest poblat no és teu");
			return null;
		}

		// Comprovació del límit de 4 ciutats
		Long numCiutats = ciutatRepo.numCiutatsJugador(j.getId());

		if(numCiutats>=4){
			System.out.println("El jugador ja té el màxim de 4 ciutats");
			return null;
		}

		// Comprovació dels recursos
		RecursJugador blat = j.getInventari().get(TipusRecurs.BLAT);
		RecursJugador mineral = j.getInventari().get(TipusRecurs.MINERAL);

		if(blat==null || mineral==null || blat.getQuantitat()<2 || mineral.getQuantitat()<3){
			System.out.println("No tens recursos suficients per millorar a ciutat");
			return null;
		}

		blat.setQuantitat(blat.getQuantitat()-2);
		mineral.setQuantitat(mineral.getQuantitat()-3);

		// Creació de la ciutat
		Ciutat c = new Ciutat(v);
		c.setJugador(j);

		v.setAssentament(c);
		
		ciutatRepo.save(c);

		return c;
	}
}
