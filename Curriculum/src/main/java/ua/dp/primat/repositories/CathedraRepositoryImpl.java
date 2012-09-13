package ua.dp.primat.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.dp.primat.domain.Cathedra;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author EniSh
 */
@Repository
public class CathedraRepositoryImpl implements CathedraRepository {
    @Transactional(readOnly=true)
    public List<Cathedra> getCathedras() {
        return em.createQuery("from Cathedra").getResultList();
    }

    public Cathedra getCathedraByName(String name) {
        try {
            return (Cathedra) em.createNamedQuery("getCathedraByName")
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Cathedra store(Cathedra cathedra) {
        final Cathedra merged = em.merge(cathedra);
        em.flush();
        cathedra.setId(merged.getId());
        return merged;
    }

    @PersistenceContext(unitName = "curriculum")
    private EntityManager em;
}
