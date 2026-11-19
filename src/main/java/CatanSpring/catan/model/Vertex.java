package CatanSpring.catan.model;


import CatanSpring.catan.model.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "vertex")
public class Vertex {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique=true)
	private Integer id;

	@Column(name = "codi", nullable = false, unique = true)
	private Integer codi;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "partida_id")
	private Partida partida_id;
	
	@OneToMany(mappedBy="vertexA", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Aresta> arestesA;
	
	@OneToMany(mappedBy="vertexB", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Aresta> arestesB;

	@OneToMany(mappedBy = "vertex")
	private Set<HexagonVertex> hexagonVertex = new HashSet<>();
	
	@OneToOne(mappedBy = "vertex")
	private Assentament assentament;
	
	// constructor hibernate
	public Vertex() {
	}
	
	// constructor import i DAO
	public Vertex (int codi) { // l'id es autogenerat, no s'ha de ficar al constructor
		this.codi = codi;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCodi(Integer codi) {
		this.codi = codi;
	}

	public Integer getCodi() {
		return codi;
	}

	public Partida getPartida_id() {
		return partida_id;
	}

	public void setPartida_id(Partida partida_id) {
		this.partida_id = partida_id;
	}

	public Set<Aresta> getArestesA() {
		return arestesA;
	}

	public void setArestesA(Set<Aresta> arestesA) {
		this.arestesA = arestesA;
	}

	public Set<Aresta> getArestesB() {
		return arestesB;
	}

	public void setArestesB(Set<Aresta> arestesB) {
		this.arestesB = arestesB;
	}

	public Set<HexagonVertex> getHexagonVertex() {
		return hexagonVertex;
	}

	public void setHexagonVertex(Set<HexagonVertex> hexagonVertex) {
		this.hexagonVertex = hexagonVertex;
	}

	public Assentament getAssentament() {
		return assentament;
	}

	public void setAssentament(Assentament assentament) {
		this.assentament = assentament;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codi, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		return codi == other.codi && id == other.id;
	}

	@Override
	public String toString() {
		return "Vertex [id=" + id + ", codi=" + codi + "]";
	}
}
