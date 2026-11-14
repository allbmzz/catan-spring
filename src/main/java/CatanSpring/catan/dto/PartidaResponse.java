package CatanSpring.catan.dto;

import java.util.ArrayList;
import java.util.List;

import CatanSpring.catan.model.*;
import CatanSpring.model.enums.EstatPartida;

// Possible error: Set/List --> llista jugadors

// A les llistes NO posem les classes del package model perquè ens donarien diversos errors: StackOverflowError, LazyinitializationException...
// Amb el jugadorResponse, només extreiem les dades necessàries dins del servei.


public record PartidaResponse(Integer id, EstatPartida estat, int puntsPerGuanyar, List<JugadorResponse> jugadors, List<HexagonResponse> hexagons) {

	public static PartidaResponse de(Partida p, List<Jugador> jugadors, List<Hexagon> hexagons) {
		List<JugadorResponse> jugadorsList = new ArrayList<>();
	    
		//Creem els jugadors dins d'un bucle per a poder assignar el torn de forma ordenada.
		for (Jugador j : jugadors) {
	        jugadorsList.add(JugadorResponse.de(j));
	    }

		//Els hexàgons es llegeixen línia a línia en el fitxer CSV, doncs ja mantenen aquest ordre del fitxer.
	    List<HexagonResponse> hexagonsList = new ArrayList<>();
	    for (Hexagon h : hexagons) {
	        hexagonsList.add(HexagonResponse.de(h));
	    }

	    return new PartidaResponse(
	        p.getId(),
	        p.getEstatPartida(),
	        p.getPuntsPerGuanyar(),
	        jugadorsList,
	        hexagonsList
	    );
	}
	
}
