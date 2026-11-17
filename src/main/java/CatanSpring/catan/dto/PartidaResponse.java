package CatanSpring.catan.dto;

import java.util.ArrayList;
import java.util.List;

import CatanSpring.catan.model.*;
import CatanSpring.model.enums.EstatPartida;

// Possible error: Set/List --> llista jugadors

// A les llistes NO posem les classes del package model perquè ens donarien diversos errors: StackOverflowError, LazyinitializationException...
// Amb el jugadorResponse, només extreiem les dades necessàries dins del servei.


public record PartidaResponse(Integer id, EstatPartida estat, int puntsPerGuanyar, List<JugadorResponse> jugadors, List<HexagonResponse> hexagons) {

	

	
}
