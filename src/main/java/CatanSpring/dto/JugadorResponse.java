package CatanSpring.dto;

import java.util.HashMap;
import java.util.Map;

import CatanSpring.model.Jugador;
import CatanSpring.model.RecursJugador;
import CatanSpring.model.enums.ColorJugador;
import CatanSpring.model.enums.TipusRecurs;

//Aqui hem de posar les dades que volem que surtin al JSON de resposta.

// --> 

//No necessaria para la práctica, pero para el examen si.
public record JugadorResponse (Integer id, String nom, ColorJugador colorJugador, Integer ordreTorn, Integer cavallersJugats, Map<TipusRecurs, Integer> inventari){

	public static JugadorResponse de(Jugador j) {
		
		// Creem un mapa simple per no posar entitats RecursJugador. 
			Map<TipusRecurs, Integer> inventari = new HashMap<>();

			// Recorrem els recursos del jugador i n'extraiem només el tipus i la quantitat
	        for (RecursJugador recurs : j.getInventari().values()) {
	            inventari.put(recurs.getTipus(), recurs.getQuantitat());
	        }
	        
	     // Construïm i retornem el DTO només amb les dades simples que aniran al JSON
	        return new JugadorResponse(
	                j.getId(),
	                j.getNom(),
	                j.getColorJugador(),
	                j.getOrdreTorn(),
	                j.getCavallersJugats(),
	                inventari
	            );
	}



	
}
