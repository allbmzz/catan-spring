package CatanSpring.catan.model;

import CatanSpring.catan.model.Construccio;
import jakarta.persistence.*;

import java.util.Objects;

//S'utilitza SINGLE TABLE per a les subclasses de Carretera i Assentament --> NO ES POT BARREJAR SINGLE TABLE AMB JOINED, PARTEIXEN DE CONSTRUCCIO, QUE FA SERVIR JOINED (REVISAR!!)
// de moment he tret la single table perque tampoc fa res
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