package ua.dp.primat.schedule.scheduleparser;

import edu.dnu.fpm.schedule.domain.EvenOddFlag;
import edu.dnu.fpm.schedule.domain.SubgroupFlag;
import edu.dnu.fpm.schedule.parser.ScheduleParser;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ua.dp.primat.domain.Lecturer;
import ua.dp.primat.domain.LecturerType;
import ua.dp.primat.domain.Room;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.lesson.*;
import ua.dp.primat.domain.workload.Discipline;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Anton Chernetskij
 */
public class ScheduleBuilderImplTest {

    private InputStream inputStream;
    private ScheduleParser parser;
    private ScheduleBuilderImpl builder;

    private static final String description1 = "Архітектура обчислювальних систем (лк.)\n" +
            "        асист. Дзюба П.А.3/25\n" +
            "    ";
    private static final String description2 = "Фізичне виховання (пр.)\n" +
            "        корпус 2\n" +
            "    ";
    private static final String description3 = "Операційні системи (лаб.)\n" +
            "        ст. викл. Сегеда Н.Є., доц. Білобородько О.І.3/45\n" +
            "    ";
    private static final String description4 = "Функціональний аналіз (пр.)\n" +
            "        доц. Сердюк М.Є.3/62\n" +
            "    ";
    private static final String description5 = "Математичні основи представлення знань (лк.)\n" +
            "        проф. Карпов О.М.3/39\n" +
            "    ";

    @Before
    public void setUp() throws Exception {
        inputStream = new FileInputStream("src/test/resources/schedule.html");
        parser = new ScheduleParser();
        builder = new ScheduleBuilderImpl();
    }

    @Test
    public void test() throws Exception {
        parser.parse(inputStream, builder);
    }

    @Test
    public void testBuildRoom() {
        Room room = builder.getRoom(description1);
        Assert.assertEquals(new Room(3L, 25L), room);

        room = builder.getRoom(description2);
        Assert.assertEquals(new Room(2L, 1L), room);
    }

    @Test
    public void testGetLecturersD1() {
        List<Lecturer> lecturers = builder.getLecturers(description1);
        Assert.assertEquals(1, lecturers.size());
        Assert.assertEquals("Дзюба П.А.", lecturers.get(0).getName());
        Assert.assertEquals(LecturerType.ASSIATANT, lecturers.get(0).getLecturerType());
    }

    @Test
    public void testGetLecturersD3() {
        List<Lecturer> lecturers = builder.getLecturers(description3);
        Assert.assertEquals(2, lecturers.size());
        Assert.assertEquals("Сегеда Н.Є.", lecturers.get(0).getName());
        Assert.assertEquals(LecturerType.SENIORLECTURER, lecturers.get(0).getLecturerType());
        Assert.assertEquals("Білобородько О.І.", lecturers.get(1).getName());
        Assert.assertEquals(LecturerType.DOCENT, lecturers.get(1).getLecturerType());
    }

    @Test
    public void testBuildDescriptionD1() {
        LessonDescription description = builder.getLessonDescription(description1, null, 0L);
        Assert.assertEquals("Архітектура обчислювальних систем", description.getDiscipline().getName());
        Assert.assertEquals(LessonType.LECTURE, description.getLessonType());
        Assert.assertEquals("Дзюба П.А.", description.getLecturer().getName());
        Assert.assertEquals(LecturerType.ASSIATANT, description.getLecturer().getLecturerType());
    }

    @Test
    public void testBuildDescriptionD2() {
        LessonDescription description = builder.getLessonDescription(description2, null, 0L);
        Assert.assertEquals("Фізичне виховання", description.getDiscipline().getName());
        Assert.assertEquals(LessonType.PRACTICE, description.getLessonType());
        Assert.assertEquals(null ,description.getLecturer());
    }

    @Test
    public void testAddLesson1(){
        String lessonName = "Архітектура обчислювальних систем (лк.)\n" +
                    "        асист. Дзюба П.А.3/25\n" +
                    "    ";
        builder.addLesson("PZ-08-1", 1, 1, lessonName, SubgroupFlag.FIRST, EvenOddFlag.ODD);
        Lesson actual = builder.getLessons().get(0);

        LessonDescription expectedLessonDescription = new LessonDescription(
                new Discipline("Архітектура обчислювальних систем", null),
                new StudentGroup("PZ-08-1"),
                9L,
                LessonType.LECTURE,
                new Lecturer("Дзюба П.А.", null, LecturerType.ASSIATANT),
                null
        );
        Lesson expected = new Lesson(1L, WeekType.DENOMINATOR, DayOfWeek.TUESDAY, new Room(3L, 25L), expectedLessonDescription);

        Assert.assertEquals(expected, actual);
    }

    @Test
        public void testAddLesson2(){
            String lessonName = "Архітектура обчислювальних систем (лк.)\n" +
                        "        асист. Дзюба П.А.3/25\n" +
                        "    ";
            builder.addLesson("PZ-08-1", 1, 1, lessonName, SubgroupFlag.FIRST, EvenOddFlag.ODD);
            Lesson actual = builder.getLessons().get(0);

            LessonDescription expectedLessonDescription = new LessonDescription(
                    new Discipline("Архітектура обчислювальних систем", null),
                    new StudentGroup("PZ-08-1"),
                    9L,
                    LessonType.LECTURE,
                    new Lecturer("Дзюба П.А.", null, LecturerType.ASSIATANT),
                    null
            );
            Lesson expected = new Lesson(1L, WeekType.DENOMINATOR, DayOfWeek.TUESDAY, new Room(3L, 25L), expectedLessonDescription);

            Assert.assertEquals(expected, actual);
        }
}
