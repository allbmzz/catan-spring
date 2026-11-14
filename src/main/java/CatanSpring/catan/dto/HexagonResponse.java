package CatanSpring.catan.dto;

import java.util.ArrayList;
import java.util.List;

import CatanSpring.catan.model.*;
import CatanSpring.model.enums.TipusTerreny;

public record HexagonResponse(
        Integer id,
        Integer codi,
        TipusTerreny terreny,
        Integer numero,
        Integer q,
        Integer r,
        boolean lladre,
        List<ConstruccioResponse> construccions
) {
    public static HexagonResponse de(Hexagon h, boolean lladre) {
        List<ConstruccioResponse> construccions = new ArrayList<>();

        // Recorrer els vèrtexs per obtenir les construccions de cada hexàgon
        for (HexagonVertex hv : h.getVertexsHexagon()) {
            Assentament a = hv.getVertex().getAssentament();

            // Només afegim els vèrtexs que tenen una construcció
            if (a != null) {
                construccions.add(ConstruccioResponse.de(hv, a));
            }
        }

        // Retornem només les dades necessàries per al JSON, sense exposar entitats JPA
        return new HexagonResponse(
                h.getId(),
                h.getCodi(),
                h.getTerreny(),
                h.getNumero(),
                h.getQ(),
                h.getR(),
                lladre,
                construccions
        );
    }
}