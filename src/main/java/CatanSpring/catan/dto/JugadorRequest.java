package CatanSpring.catan.dto;

import java.util.List;

// És l'objecte que rep el @RequestBody del POST /partida amb el JSON que envia el client

public record JugadorRequest() { // 
	
	// Rep amb RequestBody un JSON amb la llista de noms dels jugadors i, opcionalment, els punts per guanyar. 
	
	public record PartidaRequest(List<String> jugadors, Integer puntsPerGuanyar) {
		
	}

}
