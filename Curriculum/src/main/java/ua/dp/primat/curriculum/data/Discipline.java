/*
 *  
 */

package ua.dp.primat.curriculum.data;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author EniSh
 */
@Entity
@Table(name="discipline")
@NamedQueries(
@NamedQuery(name=Discipline.GET_ALL_DISCIPLINES_QUERY, query="from Discipline")
)
public class Discipline implements Serializable {
    public static final String GET_ALL_DISCIPLINES_QUERY = "getAllDisciplines";
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long disciplineId;

    @Column(name="name")
    private String name;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Cathedra cathedra;
    
    public Discipline() {
    }

    public Discipline(String name, Cathedra cathedra) {
        this.name = name;
        this.cathedra = cathedra;
    }

    public Cathedra getCathedra() {
        return cathedra;
    }

    public void setCathedra(Cathedra cathedra) {
        this.cathedra = cathedra;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
