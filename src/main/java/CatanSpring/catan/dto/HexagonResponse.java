package CatanSpring.catan.dto;

import java.util.ArrayList;
import java.util.List;

import CatanSpring.catan.model.*;
import CatanSpring.model.enums.TipusTerreny;

public record HexagonResponse(Integer id, TipusTerreny terreny, Integer numero) {
	
	
    
	public static HexagonResponse de(Hexagon h) {
		
		/*
        List<ConstruccioResponse> construccions = new ArrayList<>();

        // Recorrer els vèrtexs per obtenir les construccions de cada hexàgon
        for (HexagonVertex hv : h.getVertexsHexagon()) {
            Assentament a = hv.getVertex().getAssentament();

            // Només afegim els vèrtexs que tenen una construcció
            if (a != null) {
                construccions.add(ConstruccioResponse.de(hv, a));
            }
        }
		 */
        // Retornem només les dades necessàries per al JSON, sense exposar entitats JPA
        return new HexagonResponse(
                h.getId(),
                h.getTerreny(),
                h.getNumero()
        );
    }
}