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
import ua.dp.primat.domain.lesson.WeekType;
import ua.dp.primat.domain.workload.Discipline;
import ua.dp.primat.repositories.*;
import ua.dp.primat.schedule.services.Semester;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
        for(Lesson lesson: lessons){
            LessonDescription description = lesson.getLessonDescription();
            LessonDescription mergedDescription = mergeLessonDescription(description);
            lesson.setLessonDescription(mergedDescription);

            Room room = lesson.getRoom();
            Room mergedRoom = mergeRoom(room);
            lesson.setRoom(mergedRoom);

            List<Lesson> lessonsInTime = lessonRepository.getLessonsByTime(mergedDescription.getStudentGroup(),
                    mergedDescription.getSemester(), lesson.getDayOfWeek(), lesson.getLessonNumber());
            Lesson mergedLesson;

            if (lesson.getWeekType() == WeekType.BOTH) {
                for(Lesson oldLesson: lessonsInTime){
                    lessonRepository.remove(oldLesson);
                }
                mergedLesson = lesson;
            } else {
                Lesson oldLesson = selectForUpdate(lessonsInTime, lesson.getWeekType());
                if (oldLesson != null) {
                    lessonRepository.remove(oldLesson);
                }
                mergedLesson = lesson;
            }
            lessonRepository.store(mergedLesson);
        }
    }

    protected LessonDescription mergeLessonDescription(LessonDescription description){
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
        StudentGroup storedGroup = findGroup(group.toString());
        if (storedGroup == null) {
            studentGroupRepository.store(group);
            return group;
        } else {
            return storedGroup;
        }
    }

    private StudentGroup findGroup(String groupName) {
        StudentGroup studentGroup = new StudentGroup(groupName);
        StudentGroup dbGroup = studentGroupRepository.getGroupByCodeAndYearAndNumber(
                studentGroup.getCode(),
                studentGroup.getYear(),
                studentGroup.getNumber());
        return dbGroup;
    }

    protected Discipline mergeDiscipline(Discipline discipline){
        Cathedra cathedra = discipline.getCathedra();
        Cathedra mergedCatedra = mergeCathedra(cathedra);
        discipline.setCathedra(mergedCatedra);

        Discipline storedDiscipline = disciplineRepository.findByNameAndCathedra(discipline.getName(), discipline.getCathedra());
        if (storedDiscipline == null) {
            disciplineRepository.store(discipline);
            storedDiscipline = discipline;
        }
        return storedDiscipline;
    }

    protected Cathedra mergeCathedra(Cathedra cathedra){
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
        Room storedRoom = roomRepository.getByProps(room.getBuilding(), room.getNumber());
        if (storedRoom == null) {
            roomRepository.store(room);
            storedRoom = room;
        }
        return storedRoom;
    }

    protected Lesson selectForUpdate(List<Lesson> lessons, WeekType weekType){
        Lesson result = null;
        if (lessons.size() == 0) {
            result = null;
        } else {
            for(Lesson lesson: lessons){
                if (lesson.getWeekType() == WeekType.BOTH) {
                    result = lesson;
                    break;
                } else {
                    if (lesson.getWeekType() == weekType) {
                        result = lesson;
                        break;
                    }
                }
            }
        }
        return result;
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
