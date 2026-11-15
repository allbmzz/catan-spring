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
		//Creem els jugadors dins d'un bucle per a poder assignar el torn de forma ordenada.
		List<JugadorResponse> jugadorsList = new ArrayList<>();
		if (jugadors != null) {
			for (Jugador j : jugadors) {
				jugadorsList.add(JugadorResponse.de(j));
			}
		}

		List<HexagonResponse> hexagonsList = new ArrayList<>();
		if (hexagons != null) {
			for (Hexagon h : hexagons) {
				// Ajusta aquesta condició segons si p.getLladre() és un Hexagon o una entitat Lladre
				boolean teLladre = (p.getLladre() != null && p.getLladre().equals(h));
				hexagonsList.add(HexagonResponse.de(h, teLladre));
			}
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
