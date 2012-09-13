package ua.dp.primat.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.dp.primat.domain.Cathedra;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.workload.Discipline;

/**
 * @author pesua
 */


@Repository("disciplineRepository")
@Transactional
public class DisciplineRepositoryimpl implements DisciplineRepository {

    public void store(Discipline discipline) {
        if (em.contains(discipline) || (discipline.getId() != null)) {
            em.merge(discipline);
        } else {
            em.persist(discipline);
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Discipline> getDisciplines() {
        return em.createNamedQuery(Discipline.GET_ALL_DISCIPLINES_QUERY).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Discipline> getDisciplinesForGroupAndSemester(StudentGroup group, long semesterNumber) {
        return em.createNamedQuery(Discipline.GET_DISCIPLINES_BY_GROUP_AND_SEMESTER)
                .setParameter("sem", semesterNumber)
                .setParameter("studentGroup", group)
                .getResultList();
    }

    public void delete(Discipline discipline) {
        //load(discipline.id);
        final Discipline r = em.find(Discipline.class, discipline.getId());
        if (em.contains(r)) {
            em.remove(r);
        }
    }

    public Discipline load(Long id) {
        return em.find(Discipline.class, id);
    }

    public Discipline update(Discipline discipline) {
        return em.merge(discipline);
    }

    @SuppressWarnings("unchecked")
    public List<String> getDisciplineNamesLike(String pattern) {
        return em.createNamedQuery(Discipline.GET_DISCIPLINE_NAMES_WITH_NAME_LIKE).setParameter("pattern", pattern).getResultList();
    }

    public Discipline findByName(String name) {
        List<Discipline> resultList = em.createNamedQuery(Discipline.GET_DISCIPLINE_BY_NAME).setParameter("name", name).getResultList();
        if (resultList.size() > 0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    public Discipline findByNameAndCathedra(String name, Cathedra cathedra){
        try{
            return (Discipline) em.createNamedQuery("getDisciplinesByNameAndCathedra")
                    .setParameter("name", name)
                    .setParameter("cathedra", cathedra)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @PersistenceContext(unitName = "curriculum")
    private EntityManager em;
}
