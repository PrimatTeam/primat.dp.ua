package ua.dp.primat.schedule.scheduleparser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dp.primat.domain.Cathedra;
import ua.dp.primat.domain.Lecturer;
import ua.dp.primat.domain.Room;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.lesson.Lesson;
import ua.dp.primat.domain.lesson.LessonDescription;
import ua.dp.primat.domain.workload.Discipline;
import ua.dp.primat.repositories.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Anton Chernetskij
 */
@Service
public class SimpleLessonMergeStrategy implements LessonMergeStrategy {

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

    @PersistenceContext(unitName = "curriculum")
    private EntityManager em;

    @Transactional
    public void mergeLessons(List<Lesson> lessons){
        removeOldLessonsForGroups(lessons);
        for(Lesson lesson: lessons){
            LessonDescription description = lesson.getLessonDescription();
            LessonDescription mergedDescription = mergeLessonDescription(description);
            lesson.setLessonDescription(mergedDescription);

            Room room = lesson.getRoom();
            Room mergedRoom = mergeRoom(room);
            lesson.setRoom(mergedRoom);
            lessonRepository.store(lesson);
        }
    }

    protected void removeOldLessonsForGroups(List<Lesson> lessons){
        Set<StudentGroup> processedGroups = new HashSet<StudentGroup>();
        Calendar calendar = Calendar.getInstance();
        for(Lesson lesson: lessons){
            if (lesson.getLessonDescription() != null) {
                StudentGroup studentGroup = lesson.getLessonDescription().getStudentGroup();
                if (!processedGroups.contains(studentGroup)) {
                    processedGroups.add(studentGroup);
                    studentGroup = mergeGroup(studentGroup);
                    List<Lesson> oldLessons = lessonRepository.getLessonsByGroupAndSemester(
                            studentGroup,
                            studentGroup.getSemesterForDate(calendar));
                    for(Lesson oldLesson: oldLessons){
                        lessonRepository.remove(oldLesson);
                    }
                }
            }
        }
    }

    protected LessonDescription mergeLessonDescription(LessonDescription description){
        if (description == null) {
            return null;
        }
        StudentGroup group = description.getStudentGroup();
        StudentGroup mergedGroup = mergeGroup(group);
        description.setStudentGroup(mergedGroup);

        Discipline discipline = description.getDiscipline();
        Discipline mergedDiscipline = mergeDiscipline(discipline);
        description.setDiscipline(mergedDiscipline);

        Lecturer lecturer = description.getLecturer();
        Lecturer mergedLecturer = mergeLecturer(lecturer);
        description.setLecturer(mergedLecturer);

        Lecturer assistant = description.getAssistant();
        Lecturer mergedAssistant = mergeLecturer(assistant);
        description.setAssistant(mergedAssistant);

        LessonDescription storedDescription = findLessonDescription(description);
        if (storedDescription == null) {
            lessonDescriptionRepository.store(description);
            storedDescription = description;
        }
        return storedDescription;
    }

    protected LessonDescription findLessonDescription(LessonDescription description){
        return lessonDescriptionRepository.findByFields(
                description.getDiscipline(),
                description.getStudentGroup(),
                description.getSemester(),
                description.getLessonType(),
                description.getLecturer(),
                description.getAssistant());
    }

    protected StudentGroup mergeGroup(StudentGroup group){
        StudentGroup storedGroup = findGroup(group);
        if (storedGroup == null) {
            studentGroupRepository.store(group);
            return group;
        } else {
            return storedGroup;
        }
    }

    private StudentGroup findGroup(StudentGroup studentGroup) {
        StudentGroup dbGroup = studentGroupRepository.getGroupByFields(
                studentGroup.getCode(),
                studentGroup.getYear(),
                studentGroup.getNumber(),
                studentGroup.getGroupType());
        return dbGroup;
    }

    protected Discipline mergeDiscipline(Discipline discipline){
        Cathedra cathedra = discipline.getCathedra();
        Cathedra mergedCatedra = mergeCathedra(cathedra);
        discipline.setCathedra(mergedCatedra);

        Discipline storedDiscipline = disciplineRepository.findByName(discipline.getName());
        if (storedDiscipline == null) {
            disciplineRepository.store(discipline);
            storedDiscipline = discipline;
        }
        return storedDiscipline;
    }

    protected Cathedra mergeCathedra(Cathedra cathedra){
        if (cathedra == null) {
            return null;
        }
        Cathedra storedCathedra = findCathedra(cathedra);
        if (storedCathedra == null) {
            storedCathedra = cathedraRepository.store(cathedra);
        }
        return storedCathedra;
    }

    protected Cathedra findCathedra(Cathedra cathedra){
        return cathedraRepository.getCathedraByName(cathedra.getName());
    }

    protected Lecturer mergeLecturer(Lecturer lecturer){
        if (lecturer == null) {
            return null;
        }
        Lecturer storedLecturer = lecturerRepository.getLecturerByName(lecturer.getName());
        if (storedLecturer == null) {
            Cathedra cathedra = lecturer.getCathedra();
            Cathedra mergedCatedra = mergeCathedra(cathedra);
            lecturer.setCathedra(mergedCatedra);
            lecturerRepository.store(lecturer);
            storedLecturer = lecturer;
        }
        return storedLecturer;
    }

    protected Room mergeRoom(Room room){
        if (room == null) {
            return null;
        }
        Room storedRoom = roomRepository.getByProps(room.getBuilding(), room.getNumber());
        if (storedRoom == null) {
            roomRepository.store(room);
            storedRoom = room;
        }
        return storedRoom;
    }

    public void setStudentGroupRepository(StudentGroupRepository studentGroupRepository) {
        this.studentGroupRepository = studentGroupRepository;
    }

    public void setLessonRepository(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void setLecturerRepository(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public void setLessonDescriptionRepository(LessonDescriptionRepository lessonDescriptionRepository) {
        this.lessonDescriptionRepository = lessonDescriptionRepository;
    }

    public CathedraRepository getCathedraRepository() {
        return cathedraRepository;
    }

    public void setCathedraRepository(CathedraRepository cathedraRepository) {
        this.cathedraRepository = cathedraRepository;
    }

    public DisciplineRepository getDisciplineRepository() {
        return disciplineRepository;
    }

    public void setDisciplineRepository(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }
}
