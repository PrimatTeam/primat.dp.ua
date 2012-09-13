package ua.dp.primat.repositories;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import ua.dp.primat.domain.Lecturer;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.lesson.LessonDescription;
import ua.dp.primat.domain.lesson.LessonType;
import ua.dp.primat.domain.workload.Discipline;
import ua.dp.primat.schedule.services.Semester;

import java.util.List;

/**
 *
 * @author fdevelop
 */
@Repository("lessonDescriptionRepository")
public class LessonDescriptionRepositoryImpl implements LessonDescriptionRepository {

    public void store(LessonDescription lessonDescription) {
        final LessonDescription merged = em.merge(lessonDescription);
        em.flush();
        lessonDescription.setId(merged.getId());
    }

    public void remove(LessonDescription lessonDescription) {
        final LessonDescription l = em.find(LessonDescription.class, lessonDescription.getId());
        if (l != null) {
            em.remove(lessonDescription);
        }
    }

    @SuppressWarnings("unchecked")
    public LessonDescription findByFields(Discipline discipline, StudentGroup group, Long semester, LessonType lessonType,
                                  Lecturer lecturer, Lecturer assistant){
        Query query = em.createNamedQuery("getDescriptionByFields");
        query.setParameter("discipline", discipline);
        query.setParameter("studentGroup", group);
        query.setParameter("semester", semester);
        query.setParameter("lessonType", lessonType);
        query.setParameter("lecturer", lecturer);
        query.setParameter("assistant",assistant);
        try{
            return (LessonDescription) query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<LessonDescription> findAll(){
        return em.createNamedQuery("getAllDescriptions").getResultList();
    }


    public LessonDescription find(Long id) {
        return em.find(LessonDescription.class, id);
    }

    @PersistenceContext(unitName = "curriculum")
    private EntityManager em;
}
