package CatanSpring.catan.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;

@Entity
@DiscriminatorValue("POBLAT")
@NamedQuery(
		name="Poblat.numPoblatsJugador",
		query="SELECT COUNT(p) " +
			  "FROM Poblat p " +
		      "WHERE p.jugador.id = :id"
)
public class Poblat extends Assentament {
    //Constructors
    public Poblat() {
    }

    public Poblat(Vertex vertex) {
        super(vertex);
    }
}

