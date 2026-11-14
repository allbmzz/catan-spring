package CatanSpring.catan.dto;

import CatanSpring.catan.model.*;

public record ConstruccioResponse(
        Integer posicio,
        String tipus,
        Integer idJugador,
        String nomJugador
) {
    public static ConstruccioResponse de(HexagonVertex hv, Assentament a) {
        Jugador j = a.getJugador();

        // Diferenciem ciutats i poblats per indicar el tipus de construcció al JSON
        String tipus = a instanceof Ciutat ? "CIUTAT" : "POBLAT";

        // Incloem les dades del propietari sense retornar l'entitat Jugador
        return new ConstruccioResponse(
                hv.getPosicio(),
                tipus,
                j.getId(),
                j.getNom()
        );
    }
}