package ua.dp.primat.schedule.scheduleparser;

import org.springframework.beans.factory.annotation.Autowired;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.lesson.Lesson;
import ua.dp.primat.repositories.*;

import java.util.List;

/**
 * Created by Anton Chernetskij
 */
public class SimpleLessonMergeStrategy implements LessonMergeStrategy {     //todo implement

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

    public void mergeLessons(List<Lesson> lessons){

    }

    private StudentGroup loadGroup(String groupName) {
        StudentGroup studentGroup = new StudentGroup(groupName);
        StudentGroup dbGroup = studentGroupRepository.getGroupByCodeAndYearAndNumber(
                studentGroup.getCode(),
                studentGroup.getYear(),
                studentGroup.getNumber());
        return dbGroup;
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
}
