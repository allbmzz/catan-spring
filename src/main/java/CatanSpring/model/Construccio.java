package CatanSpring.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "construccio")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Construccio {

	// Atributs
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Relacions
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_jugador", nullable = false)
	private Jugador jugador;

	// Constructor
	public Construccio() {
	}

	public Construccio(Jugador jugador) {
		this.jugador = jugador;
	}

	// Getters i setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Construccio that = (Construccio) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}