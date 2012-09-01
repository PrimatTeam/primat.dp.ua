package ua.dp.primat.schedule.services;

import java.io.Serializable;
import ua.dp.primat.domain.workload.Discipline;
import ua.dp.primat.domain.lesson.DayOfWeek;
import ua.dp.primat.domain.Lecturer;
import ua.dp.primat.domain.lesson.Lesson;
import ua.dp.primat.domain.lesson.LessonDescription;
import ua.dp.primat.domain.Room;
import ua.dp.primat.domain.lesson.LessonType;
import ua.dp.primat.domain.lesson.WeekType;

/**
 * 
 * @author Administrator
 */
public class EditableLesson implements Serializable {

    public static EditableLesson fromLesson(Lesson lesson) {
        final EditableLesson el = new EditableLesson();

        if (lesson != null) {
            el.setId(lesson.getId());
            el.setRoom(lesson.getRoom());
            el.setWeekType(lesson.getWeekType());
            el.setLecturer(lesson.getLessonDescription().getLecturer());
            el.setAsistant(lesson.getLessonDescription().getAssistant());
            el.setDiscipline(lesson.getLessonDescription().getDiscipline());
            el.setDisciplineName(el.getDiscipline().getName());
            el.setLessonType(lesson.getLessonDescription().getLessonType());
        }

        return el;
    }

    public EditableLesson() {
        eLesson = new Lesson();
        eLesson.setLessonDescription(new LessonDescription());
    }

    public boolean isEmpty() {
        return disciplineName == null || disciplineName.isEmpty();
    }

    public Lecturer getAsistant() {
        return eLesson.getLessonDescription().getAssistant();
    }

    public void setAsistant(Lecturer asistant) {
        eLesson.getLessonDescription().setAssistant(asistant);
    }

    public LessonType getLessonType() {
        return eLesson.getLessonDescription().getLessonType();
    }

    public void setLessonType(LessonType lessonType) {
        eLesson.getLessonDescription().setLessonType(lessonType);
    }

    public Discipline getDiscipline() {
        return eLesson.getLessonDescription().getDiscipline();
    }

    public void setDiscipline(Discipline discipline) {
        eLesson.getLessonDescription().setDiscipline(discipline);
    }

    public Long getId() {
        return eLesson.getId();
    }

    public void setId(Long id) {
        eLesson.setId(id);
    }

    public Lecturer getLecturer() {
        return eLesson.getLessonDescription().getLecturer();
    }

    public void setLecturer(Lecturer lecturer) {
        eLesson.getLessonDescription().setLecturer(lecturer);
    }

    public Room getRoom() {
        return eLesson.getRoom();
    }

    public void setRoom(Room room) {
        eLesson.setRoom(room);
    }

    public WeekType getWeekType() {
        return eLesson.getWeekType();
    }

    public void setWeekType(WeekType weekType) {
        eLesson.setWeekType(weekType);
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    private Lesson eLesson;
    private String disciplineName;
    private static final long serialVersionUID = 1L;
}