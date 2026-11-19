package CatanSpring.catan.dto;

import java.util.Map;
import java.util.HashMap;

import CatanSpring.catan.model.Jugador;
import CatanSpring.catan.model.RecursJugador;
import CatanSpring.model.enums.TipusRecurs;

public record JugadorResponse(
		Integer id,
		String nom,
		Map<TipusRecurs, RecursJugadorResponse> inventari
) {

	public static JugadorResponse de(Jugador j) {

		Map<TipusRecurs, RecursJugadorResponse> inventari = new HashMap<>();

		for (Map.Entry<TipusRecurs, RecursJugador> entrada : j.getInventari().entrySet()) {
			inventari.put(entrada.getKey(), RecursJugadorResponse.de(entrada.getValue()));
		}

		return new JugadorResponse(j.getId(), j.getNom(), inventari);
	}
}