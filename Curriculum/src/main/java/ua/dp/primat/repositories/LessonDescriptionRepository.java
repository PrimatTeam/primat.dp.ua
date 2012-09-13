package ua.dp.primat.repositories;

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
public interface LessonDescriptionRepository {
    void store(LessonDescription lessonDescription);
    void remove(LessonDescription lessonDescription);
    LessonDescription find(Long id);
    LessonDescription findByFields(Discipline discipline, StudentGroup group, Long semester, LessonType lessonType,
                                      Lecturer lecturer, Lecturer assistant);
    List<LessonDescription> findAll();
}
