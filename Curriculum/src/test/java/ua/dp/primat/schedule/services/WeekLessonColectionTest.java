package ua.dp.primat.schedule.services;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.dp.primat.domain.lesson.DayOfWeek;
import ua.dp.primat.domain.lesson.Lesson;
import ua.dp.primat.domain.lesson.LessonDescription;
import ua.dp.primat.domain.lesson.LessonType;
import ua.dp.primat.domain.lesson.WeekType;
import static org.junit.Assert.*;

/**
 *
 * @author work
 */
public class WeekLessonColectionTest {

    public WeekLessonColectionTest() {
    }

    //TODO disabled
    @Test
    public void testGetDayList() {
        System.out.println("WeekLessonCollection");

//        List<Lesson> l = new ArrayList<Lesson>();
//        LessonDescription ld = new LessonDescription(null, null, 1L, LessonType.LECTURE, null, null);
//        Lesson less = new Lesson(1L, WeekType.BOTH, DayOfWeek.MONDAY, null, ld);
//        less.setId(1L);
//        l.add(less);
//
//        WeekLessonCollection instance = new WeekLessonCollection(l);
//        List<LessonItem[]> result = instance.getDayList();
//        assertEquals("Incorrect lesson list in getDayList", Long.valueOf(1), result.get(0)[0].getNumerator().getId());
//
//        assertEquals("Incorrect lesson array in getLessonItems", Long.valueOf(1), instance.getLessonItems()[0][0].getNumerator().getId());
    }
}