package CatanSpring.catan.model;

import CatanSpring.catan.model.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
		name = "vertex",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"partida_id", "codi"})
		}
)
public class Vertex {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "codi", nullable = false)
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

	public Vertex() {
	}

	public Vertex(int codi) {
		this.codi = codi;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodi() {
		return codi;
	}

	public void setCodi(Integer codi) {
		this.codi = codi;
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
		if (obj == null || getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		return Objects.equals(codi, other.codi)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Vertex [id=" + id + ", codi=" + codi + "]";
	}
}