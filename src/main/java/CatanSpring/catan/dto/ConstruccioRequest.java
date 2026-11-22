package CatanSpring.catan.dto;

public class ConstruccioRequest { // request per obtenir el tipus de construccio que vol construir el jugador.
	
	private String tipus;  // tipus de construccio a escollir
	private Long idAresta; // posicio aresta per construir una carretera
	private Long idVertex;  // posicio vertex per construir poble o ciutat
	
	public String getTipus() {
		return this.tipus;
	}
	
	public void setTipus(String tipus) {
		this.tipus = tipus;
	}
	
	public Long getIdAresta() {
		return this.idAresta;
	}
	
	public void setIdAresta(Long idAresta) {
		this.idAresta = idAresta;
	}
	
	public Long getIdVertex() {
		return this.idVertex;
	}
	
	public void setIdVertex(Long idVertex) {
		this.idVertex = idVertex;
	}
}
