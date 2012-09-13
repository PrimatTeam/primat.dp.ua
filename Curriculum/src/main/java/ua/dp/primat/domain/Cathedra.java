package ua.dp.primat.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@NamedQueries(
        @NamedQuery(name = "getCathedraByName", query = "select c from Cathedra c where c.name = :name")
)
public class Cathedra implements Serializable {

    public Cathedra() {
    }

    public Cathedra(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cathedra other = (Cathedra) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INITODDNUMBER, MULTODDNUMBER)
                .append(id)
                .append(name)
                .toHashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long cathedraId) {
        this.id = cathedraId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private static final int INITODDNUMBER = 7;
    private static final int MULTODDNUMBER = 79;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
