package ua.dp.primat.schedule.scheduleparser;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.dp.primat.domain.*;
import ua.dp.primat.domain.lesson.*;
import ua.dp.primat.domain.workload.Discipline;
import ua.dp.primat.repositories.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton Chernetskij
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class SimpleLessonMergeStrategyTest {

    @Autowired
    public SimpleLessonMergeStrategy mergeStrategy;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private LessonDescriptionRepository lessonDescriptionRepository;

    @Autowired
    private CathedraRepository cathedraRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Test
    @Transactional
    @DirtiesContext
    public void testMergeLessonDescription() {
        Cathedra cathedra = new Cathedra("CoolCathedra1");

        Discipline discipline = new Discipline("CoolDiscipline1", cathedra);
        disciplineRepository.store(discipline);

        StudentGroup studentGroup = new StudentGroup("Pz-08-1");
        studentGroupRepository.store(studentGroup);

        Lecturer lecturer11 = new Lecturer("CoolLecturer1", cathedra, LecturerType.DOCENT);
        lecturerRepository.store(lecturer11);

        Lecturer lecturer12 = new Lecturer("CoolLecturer2", cathedra, LecturerType.DOCENT);
        lecturerRepository.store(lecturer12);

        LessonDescription lessonDescription = new LessonDescription(
                discipline,
                studentGroup, 3L,
                LessonType.LECTURE,
                lecturer11,
                lecturer12);
        lessonDescriptionRepository.store(lessonDescription);

        LessonDescription sameLessonDescription = new LessonDescription(
                new Discipline("CoolDiscipline1", cathedra),
                new StudentGroup("Pz-08-1"), 3L,
                LessonType.LECTURE,
                new Lecturer("CoolLecturer1", cathedra, LecturerType.DOCENT),
                new Lecturer("CoolLecturer2", cathedra, LecturerType.DOCENT));
        mergeStrategy.mergeLessonDescription(sameLessonDescription);
        Assert.assertEquals(1, lessonDescriptionRepository.findAll().size());
        Assert.assertEquals(1, disciplineRepository.getDisciplines().size());
        Assert.assertEquals(1, studentGroupRepository.getGroups().size());
        Assert.assertEquals(2, lecturerRepository.getAllLecturers().size());

        LessonDescription newLessonDescription = new LessonDescription(
                new Discipline("CoolDiscipline2", new Cathedra("CoolCathedra1")),
                new StudentGroup("Pz-08-1"), 3L,
                LessonType.LECTURE,
                new Lecturer("CoolLecturer1", new Cathedra("CoolCathedra1"), LecturerType.DOCENT),
                new Lecturer("CoolLecturer3", new Cathedra("CoolCathedra1"), LecturerType.DOCENT));
        mergeStrategy.mergeLessonDescription(newLessonDescription);

        Assert.assertEquals(2, lessonDescriptionRepository.findAll().size());
        Assert.assertEquals(2, disciplineRepository.getDisciplines().size());
        Assert.assertEquals(1, studentGroupRepository.getGroups().size());
        Assert.assertEquals(3, lecturerRepository.getAllLecturers().size());
    }

    @Test
    @DirtiesContext
    public void testMergeLessons() {
        LessonDescription lessonDescription1 = new LessonDescription(
                new Discipline("CoolDiscipline1", new Cathedra("CoolCathedra1")),
                new StudentGroup("Pz-08-1"), 3L,
                LessonType.LECTURE,
                new Lecturer("CoolLecturer1", new Cathedra("CoolCathedra1"), LecturerType.DOCENT),
                new Lecturer("CoolLecturer2", new Cathedra("CoolCathedra1"), LecturerType.DOCENT));
        Lesson lesson1 = new Lesson(1L, WeekType.NUMERATOR, DayOfWeek.FRIDAY, new Room(3L, 3L), lessonDescription1);

        mergeStrategy.mergeLessons(Arrays.asList(lesson1));

        Assert.assertEquals(1, lessonRepository.getAllLessons().size());
        Assert.assertEquals(1, lessonDescriptionRepository.findAll().size());
        Assert.assertEquals(1, disciplineRepository.getDisciplines().size());
        Assert.assertEquals(1, studentGroupRepository.getGroups().size());
        Assert.assertEquals(2, lecturerRepository.getAllLecturers().size());

        LessonDescription lessonDescription2 = new LessonDescription(
                new Discipline("CoolDiscipline2", new Cathedra("CoolCathedra1")),
                new StudentGroup("Pz-08-1"), 3L,
                LessonType.LECTURE,
                new Lecturer("CoolLecturer1", new Cathedra("CoolCathedra1"), LecturerType.DOCENT),
                new Lecturer("CoolLecturer3", new Cathedra("CoolCathedra1"), LecturerType.DOCENT));
        Lesson lesson2 = new Lesson(1L, WeekType.DENOMINATOR, DayOfWeek.FRIDAY, new Room(3L, 3L), lessonDescription2);

        mergeStrategy.mergeLessons(Arrays.asList(lesson2));

        Assert.assertEquals(2, lessonRepository.getAllLessons().size());
        Assert.assertEquals(2, lessonDescriptionRepository.findAll().size());
        Assert.assertEquals(2, disciplineRepository.getDisciplines().size());
        Assert.assertEquals(1, studentGroupRepository.getGroups().size());
        Assert.assertEquals(3, lecturerRepository.getAllLecturers().size());

        LessonDescription lessonDescription3 = new LessonDescription(
                new Discipline("CoolDiscipline3", new Cathedra("CoolCathedra1")),
                new StudentGroup("Pz-08-1"), 3L,
                LessonType.LECTURE,
                new Lecturer("CoolLecturer1", new Cathedra("CoolCathedra1"), LecturerType.DOCENT),
                new Lecturer("CoolLecturer2", new Cathedra("CoolCathedra1"), LecturerType.DOCENT));
        Lesson lesson3 = new Lesson(1L, WeekType.DENOMINATOR, DayOfWeek.FRIDAY, new Room(3L, 3L), lessonDescription3);
        mergeStrategy.mergeLessons(Arrays.asList(lesson3));

        Assert.assertEquals(2, lessonRepository.getAllLessons().size());
        Assert.assertEquals(2, lessonDescriptionRepository.findAll().size());
        Assert.assertEquals(3, disciplineRepository.getDisciplines().size());
        Assert.assertEquals(1, studentGroupRepository.getGroups().size());
        Assert.assertEquals(3, lecturerRepository.getAllLecturers().size());

        LessonDescription lessonDescription4 = new LessonDescription(
                new Discipline("CoolDiscipline4", new Cathedra("CoolCathedra1")),
                new StudentGroup("Pz-08-1"), 3L,
                LessonType.LECTURE,
                new Lecturer("CoolLecturer1", new Cathedra("CoolCathedra1"), LecturerType.DOCENT),
                new Lecturer("CoolLecturer3", new Cathedra("CoolCathedra1"), LecturerType.DOCENT));
        Lesson lesson4 = new Lesson(1L, WeekType.BOTH, DayOfWeek.FRIDAY, new Room(3L, 3L), lessonDescription4);
        mergeStrategy.mergeLessons(Arrays.asList(lesson4));

        Assert.assertEquals(1, lessonRepository.getAllLessons().size());
        Assert.assertEquals(1, lessonDescriptionRepository.findAll().size());
        Assert.assertEquals(4, disciplineRepository.getDisciplines().size());
        Assert.assertEquals(1, studentGroupRepository.getGroups().size());
        Assert.assertEquals(3, lecturerRepository.getAllLecturers().size());

        Lesson lesson = lessonRepository.getAllLessons().get(0);
        Assert.assertEquals("CoolDiscipline4", lesson.getLessonDescription().getDiscipline().getName());
    }

    @Test
    @DirtiesContext
    public void testMergeGroups() throws Exception {
        studentGroupRepository.store(new StudentGroup("Pz-08-1"));
        mergeStrategy.mergeGroup(new StudentGroup("Pz-08-1"));
        List<StudentGroup> groups = studentGroupRepository.getGroups();
        Assert.assertEquals(1, groups.size());
        Assert.assertTrue(groups.contains(new StudentGroup("Pz-08-1")));

        mergeStrategy.mergeGroup(new StudentGroup("Pz-08-2"));
        groups = studentGroupRepository.getGroups();
        Assert.assertEquals(2, groups.size());
        Assert.assertTrue(groups.contains(new StudentGroup("Pz-08-1")));
        Assert.assertTrue(groups.contains(new StudentGroup("Pz-08-2")));
    }

    @Test
    @DirtiesContext
    public void testMergeCathedra() {
        cathedraRepository.store(new Cathedra("Cool cathedra 1"));
        mergeStrategy.mergeCathedra(null);
        mergeStrategy.mergeCathedra(new Cathedra("Cool cathedra 1"));
        List<Cathedra> cathedras = cathedraRepository.getCathedras();
        Assert.assertEquals(1, cathedras.size());
        Assert.assertTrue(cathedras.contains(new Cathedra("Cool cathedra 1")));

        mergeStrategy.mergeCathedra(new Cathedra("Cool cathedra 2"));
        cathedras = cathedraRepository.getCathedras();
        Assert.assertEquals(2, cathedras.size());
        Assert.assertTrue(cathedras.contains(new Cathedra("Cool cathedra 1")));
        Assert.assertTrue(cathedras.contains(new Cathedra("Cool cathedra 2")));
    }

    @Test
    @DirtiesContext
    public void testMergeLecturer() {
        Cathedra cathedra = new Cathedra("Cool cathedra");
        cathedraRepository.store(cathedra);

        lecturerRepository.store(new Lecturer("Cool lecturer 1", cathedra, LecturerType.DOCENT));
        mergeStrategy.mergeLecturer(new Lecturer("Cool lecturer 1", cathedra, LecturerType.DOCENT));
        List<Lecturer> lecturers = lecturerRepository.getAllLecturers();
        Assert.assertEquals(1, lecturers.size());
        Assert.assertTrue(lecturers.contains(new Lecturer("Cool lecturer 1", cathedra, LecturerType.DOCENT)));

        mergeStrategy.mergeLecturer(new Lecturer("Cool lecturer 2", cathedra, LecturerType.DOCENT));
        lecturers = lecturerRepository.getAllLecturers();
        Assert.assertEquals(2, lecturers.size());
        Assert.assertTrue(lecturers.contains(new Lecturer("Cool lecturer 1", cathedra, LecturerType.DOCENT)));
        Assert.assertTrue(lecturers.contains(new Lecturer("Cool lecturer 2", cathedra, LecturerType.DOCENT)));
    }

    @Test
    @DirtiesContext
    public void testMergeRoom() {
        roomRepository.store(new Room(3L, 3L));
        mergeStrategy.mergeRoom(new Room(3L, 3L));
        List<Room> rooms = roomRepository.getRooms();
        Assert.assertEquals(1, rooms.size());
        Assert.assertTrue(rooms.contains(new Room(3L, 3L)));

        mergeStrategy.mergeRoom(new Room(3L, 6L));
        rooms = roomRepository.getRooms();
        Assert.assertEquals(2, rooms.size());
        Assert.assertTrue(rooms.contains(new Room(3L, 3L)));
        Assert.assertTrue(rooms.contains(new Room(3L, 6L)));
    }


    @Test
    @DirtiesContext
    @Transactional
    public void testMergeDiscipline() {
        disciplineRepository.store(new Discipline("Cool discipline 1", new Cathedra("Cool cathedra 3")));
        mergeStrategy.mergeDiscipline(new Discipline("Cool discipline 1", new Cathedra("Cool cathedra 3")));
        List<Discipline> disciplines = disciplineRepository.getDisciplines();
        Assert.assertEquals(1, disciplines.size());
        Assert.assertTrue(disciplines.contains(new Discipline("Cool discipline 1", new Cathedra("Cool cathedra 3"))));
        Assert.assertEquals(1, cathedraRepository.getCathedras().size());

        mergeStrategy.mergeDiscipline(new Discipline("Cool discipline 2", new Cathedra("Cool cathedra 3")));
        disciplines = disciplineRepository.getDisciplines();
        Assert.assertEquals(2, disciplines.size());
        Assert.assertEquals(1, cathedraRepository.getCathedras().size());
        Assert.assertTrue(disciplines.contains(new Discipline("Cool discipline 1", new Cathedra("Cool cathedra 3"))));
    }

    @Test
    @DirtiesContext
    public void testSelectForUpdateTwoLessons() {
        List<Lesson> lessons = new ArrayList<Lesson>();
        Lesson lesson1 = new Lesson(null, WeekType.NUMERATOR, null, new Room(0L, 0L), null);
        Lesson lesson2 = new Lesson(null, WeekType.DENOMINATOR, null, new Room(1L, 1L), null);
        lessons.add(lesson1);
        lessons.add(lesson2);

        Lesson actual = mergeStrategy.selectForUpdate(lessons, WeekType.NUMERATOR);
        Assert.assertEquals(lesson1, actual);

        actual = mergeStrategy.selectForUpdate(lessons, WeekType.DENOMINATOR);
        Assert.assertEquals(lesson2, actual);
    }


    @Test
    @DirtiesContext
    public void testSelectForUpdateSingleLesson() {
        List<Lesson> lessons = new ArrayList<Lesson>();
        Lesson lesson = new Lesson(null, WeekType.BOTH, null, new Room(0L, 0L), null);
        lessons.add(lesson);

        Lesson actual = mergeStrategy.selectForUpdate(lessons, WeekType.NUMERATOR);
        Assert.assertEquals(lesson, actual);

        actual = mergeStrategy.selectForUpdate(lessons, WeekType.DENOMINATOR);
        Assert.assertEquals(lesson, actual);

        actual = mergeStrategy.selectForUpdate(lessons, WeekType.BOTH);
        Assert.assertEquals(lesson, actual);
    }

    @Test
    public void testSelectForUpdateNoLesson() {
        List<Lesson> lessons = new ArrayList<Lesson>();
        Lesson actual = mergeStrategy.selectForUpdate(lessons, WeekType.NUMERATOR);
        Assert.assertNull(actual);
    }

}
