package ua.dp.primat.schedule.scheduleparser;

import ua.dp.primat.domain.lesson.Lesson;

import java.util.List;

/**
 * Created by Anton Chernetskij
 */
public interface LessonMergeStrategy {
    void mergeLessons(List<Lesson> lessons);
}
