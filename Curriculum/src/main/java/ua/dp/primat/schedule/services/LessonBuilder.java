package ua.dp.primat.schedule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.dp.primat.domain.lesson.DayOfWeek;
import ua.dp.primat.domain.lesson.Lesson;
import ua.dp.primat.domain.lesson.LessonDescription;

/**
 * User: EniSh
 * Date: 23.02.11
 * Time: 23:48
 */
@Component
public class LessonBuilder {
    public Lesson convertFromExternal(EditableLesson eLesson, DayOfWeek day, Long lessonNum) {
        final Lesson lesson = new Lesson();
        lesson.setId(eLesson.getId());
        lesson.setRoom(eLesson.getRoom());
        lesson.setWeekType(eLesson.getWeekType());
        lesson.setDayOfWeek(day);
        lesson.setLessonNumber(lessonNum + 1);

        final LessonDescription description = new LessonDescription();
        description.setAssistant(eLesson.getAsistant());
        description.setLecturer(eLesson.getLecturer());
        description.setDiscipline(lessonService.getDisciplineByName(eLesson.getDisciplineName()));
        description.setLessonType(eLesson.getLessonType());

        lesson.setLessonDescription(description);
        return lesson;
    }

    @Autowired
    private LessonService lessonService;
}
