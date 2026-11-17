package CatanSpring.catan.dto;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import CatanSpring.catan.model.*;
import CatanSpring.model.enums.EstatPartida;

// Possible error: Set/List --> llista jugadors

// A les llistes NO posem les classes del package model perquè ens donarien diversos errors: StackOverflowError, LazyinitializationException...
// Amb el jugadorResponse, només extreiem les dades necessàries dins del servei.


public record PartidaResponse(Integer id, EstatPartida estat, Integer puntsPerGuanyar, Set<JugadorResponse> jugadors, Set<HexagonResponse> hexagons) {


	public static PartidaResponse de(Partida p) {
		
		HashSet<JugadorResponse> jugadors = new HashSet<>();
		for (Jugador j : p.getJugadors()) {
			jugadors.add(JugadorResponse.de(j));
		}
		
		HashSet<HexagonResponse> hexagons = new HashSet<>();
		for (Hexagon h : p.getHexagons()) {
			hexagons.add(HexagonResponse.de(h));
		}
		
		return new PartidaResponse(p.getId(), p.getEstatPartida(), p.getPuntsPerGuanyar(), jugadors, hexagons);
	}
	

	
}
