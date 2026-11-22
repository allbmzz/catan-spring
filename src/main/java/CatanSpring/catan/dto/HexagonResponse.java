package CatanSpring.catan.dto;

import CatanSpring.catan.model.*;
import CatanSpring.model.enums.TipusTerreny;

public record HexagonResponse(Integer id, TipusTerreny terreny, Integer numero) {
	    
	public static HexagonResponse de(Hexagon h) {
		
        // Retornem només les dades necessàries per al JSON, sense exposar entitats JPA
        return new HexagonResponse(
                h.getId(),
                h.getTerreny(),
                h.getNumero()
        );
    }
}