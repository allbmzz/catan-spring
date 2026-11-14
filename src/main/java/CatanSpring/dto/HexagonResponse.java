package CatanSpring.dto;

import CatanSpring.model.Hexagon;

import CatanSpring.model.enums.TipusTerreny;

public record HexagonResponse(Integer id, Integer codi, TipusTerreny terreny, Integer numero, Integer q, Integer r) {
    public static HexagonResponse de(Hexagon h) {
        // Construïm el DTO només amb les dades simples de l'hexàgon, deixant fora les relacions LAZY (partida_id i vertexsHexagon) per evitar errors al JSON
        return new HexagonResponse(
            h.getId(),
            h.getCodi(),
            h.getTerreny(),
            h.getNumero(),
            h.getQ(),
            h.getR()
        );
    }
}