package ua.dp.primat.schedule.scheduleparser;

import edu.dnu.fpm.schedule.domain.EvenOddFlag;
import edu.dnu.fpm.schedule.domain.SubgroupFlag;
import edu.dnu.fpm.schedule.parser.ScheduleBuilder;
import ua.dp.primat.domain.Lecturer;
import ua.dp.primat.domain.Room;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.lesson.DayOfWeek;
import ua.dp.primat.domain.lesson.Lesson;
import ua.dp.primat.domain.lesson.LessonDescription;
import ua.dp.primat.domain.lesson.WeekType;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anton Chernetskij
 */
public class ScheduleBuilderImpl implements ScheduleBuilder {     //todo implement

    private final int year = year();

    private final int semester = semester();

    private List<Lesson> lessons = new LinkedList<Lesson>();

    private int year(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    private int semester(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        return (1 + month/6) % 2;
    }

    public void addGroup(String s) {
    }

    public void addLesson(String groupName, int dayNumber, int lessonNumber,
                          String lessonName, SubgroupFlag subgroupFlag, EvenOddFlag evenOddFlag) {
        StudentGroup group = new StudentGroup(groupName);

        Lesson lesson = new Lesson();
        lesson.setDayOfWeek(DayOfWeek.fromNumber(dayNumber));
        lesson.setLessonNumber((long) lessonNumber);
        lesson.setWeekType(mapWeekType(evenOddFlag));
        lesson.setRoom(buildRoom(lessonName));
        lesson.setLessonDescription(buildLessonDescription(lessonName, group, year, semester));

        lessons.add(lesson);
    }

    protected WeekType mapWeekType(EvenOddFlag evenOdd) {
        switch (evenOdd) {
            case EVEN:
                return WeekType.NUMERATOR;
            case ODD:
                return WeekType.DENOMINATOR;
            case ALL:
                return WeekType.BOTH;
            default:
                return null;
        }
    }

    protected Room buildRoom(String lessonDescription) {
        return null;
    }

    protected LessonDescription buildLessonDescription(String lessonDescription, StudentGroup group, int year, int semester) {
        return null;
    }

    protected Lecturer extractLecturer(String lessonDescription){
        return null;
    }

    protected Lecturer extractAssistant(String lessonDescription){
        return null;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
}
