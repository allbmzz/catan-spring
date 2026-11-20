package CatanSpring.catan.dto;

import java.util.ArrayList;
import java.util.List;

import CatanSpring.catan.model.Assentament;
import CatanSpring.catan.model.Hexagon;
import CatanSpring.catan.model.HexagonVertex;
import CatanSpring.model.enums.TipusTerreny;

public record TaulerHexagonResponse (Integer id, TipusTerreny terreny, Integer numero, boolean lladre, List<ConstruccioResponse> construccions){
	
	public static TaulerHexagonResponse de(Hexagon h, boolean teLladre) {
		List<ConstruccioResponse> construccions = new ArrayList<>();
		
		//Recorre els vèrtexs asscoiats al hexàgon
		for (HexagonVertex hv : h.getVertexsHexagon()) {
            
			//Obté l'assentament que hi ha al vèrtex si existeix
			Assentament assentament = hv.getVertex().getAssentament();

            //Si el vèrtex té un poblat o una ciutat, ho posa a la resposta.
            if (assentament != null) {
                construccions.add(ConstruccioResponse.de(hv, assentament));
            }
        }
		
		//Retorna el DTO amb les dades de l'hexagon i les seves construccions.
		 return new TaulerHexagonResponse(
	                h.getId(),
	                h.getTerreny(),
	                h.getNumero(),
	                teLladre,
	                construccions);
		
	}
	
}
