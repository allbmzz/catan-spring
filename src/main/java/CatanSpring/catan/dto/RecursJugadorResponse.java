package CatanSpring.catan.dto;

import CatanSpring.catan.model.RecursJugador;
import CatanSpring.model.enums.TipusRecurs;

public record RecursJugadorResponse(
        Integer id,
        TipusRecurs tipus,
        Integer quantitat
) {
    public static RecursJugadorResponse de(RecursJugador r) {
        return new RecursJugadorResponse(r.getId(), r.getTipus(), r.getQuantitat());
    }
}