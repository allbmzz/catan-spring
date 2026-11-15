
import java.util.HashMap;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import CatanSpring.catan.model.Hexagon;
import CatanSpring.catan.model.Vertex;
import CatanSpring.model.gestioFitxers.ImportarFitxer;

class ImportarFitxerTest {

    @Test
    @SuppressWarnings("unchecked")
    void testCarregaCSVs() {
        HashMap<Integer, Vertex> mapaVertex = new HashMap<>();
        HashMap<Integer, Hexagon> mapaHexagons = new HashMap<>();

        HashSet<Vertex> vertexs = ImportarFitxer.importar("vertexs.csv", mapaVertex, mapaHexagons);
        HashSet<Hexagon> hexagons = ImportarFitxer.importar("hexagons.csv", mapaVertex, mapaHexagons);
        var relacions = ImportarFitxer.importar("hexagon_vertex.csv", mapaVertex, mapaHexagons);
        var arestes = ImportarFitxer.importar("arestes.csv", mapaVertex, mapaHexagons);

        System.out.println("Vèrtexs carregats: " + vertexs.size());
        System.out.println("Hexàgons carregats: " + hexagons.size());
        System.out.println("Relacions carregades: " + relacions.size());
        System.out.println("Arestes carregades: " + arestes.size());

        assertEquals(19, hexagons.size(), "Haurien d'haver-hi 19 hexàgons");
        assertEquals(54, vertexs.size(), "Haurien d'haver-hi 54 vèrtexs");
    }
}