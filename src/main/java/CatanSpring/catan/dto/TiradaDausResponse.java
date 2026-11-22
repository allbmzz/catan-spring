package CatanSpring.catan.dto;

import java.util.HashSet;
import java.util.Set;

import CatanSpring.catan.model.Jugador;
import CatanSpring.catan.model.Partida;

public record TiradaDausResponse(Integer idPartida, Integer tirada, boolean lladrePendent, Set<JugadorResponse> jugadors) {

	public static TiradaDausResponse de(Partida partida) {
		
		Set<JugadorResponse> jugadors = new HashSet<>();
		
		for(Jugador j : partida.getJugadors()) {
			jugadors.add(JugadorResponse.de(j));
		}
		
		return new TiradaDausResponse(partida.getId(), partida.getUltimaTirada(), partida.isLladrePendent(), jugadors);
		
	}
	
}
