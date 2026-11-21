
package CatanSpring.catan.service;

import java.util.Optional;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import CatanSpring.catan.dto.ConstruccioRequest;
import CatanSpring.catan.dto.JugadorResponse;
import CatanSpring.catan.repository.JugadorRepository;
import CatanSpring.model.enums.*;
import CatanSpring.catan.model.*;

@Service
public class JugadorService {

	// Injecció per constructor: no cal @Autowired si només hi ha un constructor
	private final JugadorRepository repo;

	public JugadorService(JugadorRepository repo) {
		this.repo = repo;
	}

	@Transactional(readOnly = true)
	public Optional<JugadorResponse> mostrarRecursos(Long idJugador) {
		// Busquem el jugador i el transformem a DTO si existeix
		// Així evitem retornar directament l'entitat JPA
		return repo.findById(idJugador).map(JugadorResponse::de);
	}
	
	
	
	
	/*
	 * 
	 * PASSAR TORN I CONSTRUIR
	 * 
	@Transactional
	public String passarTorn(Long idJugador) {
		
		Jugador jugador = repo.findById(idJugador).orElse(null);
		
		if (jugador == null) { 
			return "No s'ha trobat el jugador";
		}
		
		// Rebutjar si la partida ha finalitzat
		Partida partida = jugador.getPartida_id();
		if (partida == null) { 
			return "No s'ha trobat la partida"; 
		} else {
			if (partida.getEstatPartida().equals(EstatPartida.EN_CURS)) {
				
				// Comprovacions de seguretat. Si encara es el torn del jugador
				if (partida.getActiu() == null || !partida.getActiu().getId().equals(idJugador.intValue())) {  // com que idJugador es un Long i el metode getId retorna un integer, fem un cast a idJugador
					return "Encara no es el torn del jugador"; 
				}
				
				// Comprovacions de seguretat. Si no s'han tirat els daus prèviament.
				if(!partida.isDausTirats()) {
					return "Encara no s'ha tirat els daus";
				}
				
				int seguentTorn = jugador.getOrdreTorn() + 1;
				
				//Quan hagin passat tots els torns, tornem al primer i anem incrementant.
				if(seguentTorn > partida.getJugadors().size()) {
					seguentTorn = 1;
					partida.setRonda(partida.getRonda() + 1);
				}
				
				//Assignar el nou jugador actiu amb un bucle.
				for (Jugador j : partida.getJugadors()) {
					if (j.getOrdreTorn() == seguentTorn) {
						partida.setActiu(j);
						break;
					}
				}
				
				//Reiniciar el daus.
				partida.setDausTirats(false);
				
				return "Torn passat correctament";
			} else if (partida.getEstatPartida().equals(EstatPartida.PREPARACIO)){
				
				
				// Comprovar que és el torn del jugador
			    // Comprovar que ha col·locat poblat i carretera
			    // Gestionar el torn en serp
			    // Si tots han acabat les dues col·locacions:
			    // Canviar estat a EN_CURS
				
				// Comprovacions de seguretat. Si encara es el torn del jugador
				if (partida.getActiu() == null || !partida.getActiu().getId().equals(idJugador.intValue())) {  
					return "Encara no es el torn del jugador"; 
				}
				
				
				
			}
		}	
	}
	
	@Transactional
	public String construir(Long idJugador, ConstruccioRequest request) {
		
		Jugador jugador = repo.findById(idJugador).orElse(null);
		

		if (jugador == null) { 
			return "No s'ha trobat el jugador";
		}
		
		if (request.getTipus().toUpperCase().equals("CARRETERA")) {
			Carretera c = construirCarretera(jugador, request.getIdAresta());
			
		} else if (request.getTipus().toUpperCase().equals("POBLAT")) {
			
		} else {
			
		}
		
	}
	
	
	
	private Carretera ConstruirCarretera(int idJugador, int idAresta) {
		
		Jugador j = repo.findById(idJugador).orElse(null);
		
		if (j==null) return null;

		Aresta a = session.createNamedQuery("Aresta.infoAresta", Aresta.class)
				.setParameter("id", idAresta)
				.uniqueResult();
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
			System.out.println("No és el torn del jugador amb id: "+idJugador);
			return null;
		}

		// Comprovació del límit de 15 carreteres per jugador
		Long numCarreteres = session.createNamedQuery("Carretera.numCarreteresJugador", Long.class)
				.setParameter("id", idJugador)
				.uniqueResult();

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

		session.persist(c);

		return c;
	}
	

	private Poblat ConstruirPoblat(Session session, int idJugador, int idVertex) {
		Jugador j = session.createNamedQuery("Jugador.infoJugador", Jugador.class)
				.setParameter("id", idJugador)
				.uniqueResult();
		if (j==null) return null;

		Vertex v = session.createNamedQuery("Vertex.infoVertex", Vertex.class)
				.setParameter("id", idVertex)
				.uniqueResult();
		if (v==null) return null;

		if(j.getPartida_id().getId()!=v.getPartida_id().getId()){
			System.out.println("El vertex i el jugador no pertanyen a la mateixa partida");
			return null;
		}

		// Comprovació del torn del jugador
		if(!j.getPartida_id().getActiu().equals(j)){
			System.out.println("No és el torn del jugador amb id: "+idJugador);
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
		Long numPoblats = session.createNamedQuery("Poblat.numPoblatsJugador", Long.class)
				.setParameter("id", idJugador)
				.uniqueResult();

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

		session.persist(p);

		// Repartiment dels recursos inicials del segon poblat
		// Pendent d'implementar segons les relacions entre vertex i terrenys

		return p;
	}


	private Ciutat MillorarACiutat(Session session, int idJugador, int idVertex) {
		Jugador j = session.createNamedQuery("Jugador.infoJugador", Jugador.class)
				.setParameter("id", idJugador)
				.uniqueResult();
		if (j==null) return null;

		Vertex v = session.createNamedQuery("Vertex.infoVertex", Vertex.class)
				.setParameter("id", idVertex)
				.uniqueResult();
		if (v==null) return null;

		if(j.getPartida_id().getId()!=v.getPartida_id().getId()){
			System.out.println("El vertex i el jugador no pertanyen a la mateixa partida");
			return null;
		}

		// Comprovació del torn del jugador
		if(!j.getPartida_id().getActiu().equals(j)){
			System.out.println("No és el torn del jugador amb id: "+idJugador);
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
		Long numCiutats = session.createNamedQuery("Ciutat.numCiutatsJugador", Long.class)
				.setParameter("id", idJugador)
				.uniqueResult();

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

		session.remove(p);
		session.persist(c);

		return c;
	}
	
	*/
}
