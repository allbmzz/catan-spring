package CatanSpring.catan.dto;

public class ConstruccioRequest { // request per obtenir el tipus de construccio que vol construir el jugador.
	
	private String tipus;  // tipus de construccio a escollir
	private int idAresta; // posicio aresta per construir una carretera
	private int idVertex;  // posicio vertex per construir poble o ciutat
	
	public String getTipus() {
		return this.tipus;
	}
	
	public void setTipus(String tipus) {
		this.tipus = tipus;
	}
	
	public int getIdAresta() {
		return this.idAresta;
	}
	
	public void setIdAresta(int idAresta) {
		this.idAresta = idAresta;
	}
	
	public int getIdVertex() {
		return this.idVertex;
	}
	
	public void setIdVertex(int idVertex) {
		this.idVertex = idVertex;
	}
}
