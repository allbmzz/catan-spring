package CatanSpring.catan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "assentament")
public abstract class Assentament extends Construccio {

	// Relacions
	@OneToOne
	@JoinColumn(name = "vertex_id", unique = true, nullable = false)
	private Vertex vertex;

	// Constructors
	public Assentament() {
	}

	public Assentament(Vertex vertex) {
		this.vertex = vertex;
	}

	// Getters i setters de Vertex
	public Vertex getVertex() {
		return vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}
}