package CatanSpring.catan.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;

@Entity
@DiscriminatorValue("CIUTAT")
@NamedQuery(
		name="Ciutat.numCiutatsJugador",
		query="SELECT COUNT(c) " +
			  "FROM Ciutat c " +
			  "WHERE c.jugador.id = :id"
)
public class Ciutat extends Assentament {
    //Constructors
    public Ciutat() {
    }

    public Ciutat(Vertex vertex) {
        super(vertex);
    }
}
