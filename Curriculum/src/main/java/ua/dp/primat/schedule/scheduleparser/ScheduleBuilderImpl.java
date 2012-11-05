package ua.dp.primat.schedule.scheduleparser;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import edu.dnu.fpm.schedule.domain.EvenOddFlag;
import edu.dnu.fpm.schedule.domain.SubgroupFlag;
import edu.dnu.fpm.schedule.parser.ScheduleBuilder;
import ua.dp.primat.domain.Lecturer;
import ua.dp.primat.domain.LecturerType;
import ua.dp.primat.domain.Room;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.lesson.*;
import ua.dp.primat.domain.workload.Discipline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Chernetskij
 */
public class ScheduleBuilderImpl implements ScheduleBuilder {     //todo implement

//    private static final Logger LOGGER = Logger.getLogger(ScheduleBuilderImpl.class.getName());
    private static final Log LOGGER = LogFactoryUtil.getLog(ScheduleBuilderImpl.class);
    private static final Pattern ROOM_PATTERN = Pattern.compile("(\\d{1,2})/(\\d{1,2})");
    private static final Pattern DISCIPLINE_PATTERN = Pattern.compile("^(.*)\\s\\((лк.|пр.|лаб.)\\)");
    private static final Pattern LECTURER_PATTERN =
            Pattern.compile("(асист\\.|ст\\. викл\\.|доц\\.|проф\\.)\\s(\\S*\\s(\\S\\.){1,2})");

    private List<Lesson> lessons = new LinkedList<Lesson>();

    public void addGroup(String s) {
    }

    public void addLesson(String groupName, int dayNumber, int lessonNumber,
                          String lessonName, SubgroupFlag subgroupFlag, EvenOddFlag evenOddFlag) {
        try{
            groupName = groupName.trim();
            StudentGroup group = new StudentGroup(groupName);

            Lesson lesson = new Lesson();
            lesson.setDayOfWeek(DayOfWeek.fromNumber(dayNumber));
            lesson.setLessonNumber((long) lessonNumber  + 1);
            lesson.setWeekType(mapWeekType(evenOddFlag));
            lesson.setSubgroup(mapSubgroup(subgroupFlag));
            lesson.setRoom(getRoom(lessonName));
            lesson.setLessonDescription(getLessonDescription(lessonName, group, getSemester(group)));

            lessons.add(lesson);
        } catch (Exception e) {
            String message = String.format("Error parsing lesson. name: %s, group: %s, dayNumber: %s, weekType: %s, subgroup: %s",
                    lessonName, groupName, dayNumber, evenOddFlag, subgroupFlag);
            LOGGER.error(message, e);
        }
    }

    protected long getSemester(StudentGroup group){
        Calendar calendar = Calendar.getInstance();
        return group.getSemesterForDate(calendar);
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

    protected Room getRoom(String lessonDescription) {
        Matcher matcher = ROOM_PATTERN.matcher(lessonDescription);
        if (matcher.find()) {
            long buildingNumber = Long.valueOf(matcher.group(1));
            long roomNumber = Long.valueOf(matcher.group(2));
            return new Room(buildingNumber, roomNumber);
        } else if (lessonDescription.contains("корпус 2")) {
            return new Room(2L, 1L);
        } else {
            return null;
        }
    }

    protected LessonDescription getLessonDescription(String lessonDescription, StudentGroup group, long semester) {
        Matcher disciplineMatcher = DISCIPLINE_PATTERN.matcher(lessonDescription.replaceAll("\\n", " "));
        if(disciplineMatcher.find()){
            String disciplineName = disciplineMatcher.group(1).trim();
            LessonType lessonType = getLessonType(disciplineMatcher.group(2));

            List<Lecturer> lecturers = getLecturers(lessonDescription);
            Lecturer lecturer = null;
            Lecturer assistant = null;
            if (lecturers.size() > 0) {
                lecturer = lecturers.get(0);
            }
            if(lecturers.size() > 1){
                assistant = lecturers.get(1);
            }

            return new LessonDescription(
                    new Discipline(disciplineName, null),
                    group,
                    semester,
                    lessonType, lecturer, assistant);

        } else {
            return null;
        }
    }

    protected LessonType getLessonType(String typeName) {
        if ("лк.".equals(typeName)) {
            return LessonType.LECTURE;
        } else if ("пр.".equals(typeName)) {
            return LessonType.PRACTICE;
        } else if ("лаб.".equals(typeName)) {
            return LessonType.LABORATORY;
        }
        throw new RuntimeException("Unknown lesson type \"" + typeName + "\"");
    }

    protected List<Lecturer> getLecturers(String lessonDescription) {
        String lecturersLine = lessonDescription.split("\n")[1];
        Matcher matcher = LECTURER_PATTERN.matcher(lessonDescription);
        List<Lecturer> lecturers = new ArrayList<Lecturer>();
        while (matcher.find()) {
            LecturerType lecturerType = getLecturerType(matcher.group(1));
            String lecturerName = matcher.group(2);
            lecturers.add(new Lecturer(lecturerName, null, lecturerType));
        }
        return lecturers;
    }

    protected LecturerType getLecturerType(String type) {
        if ("проф.".equals(type)) {
            return LecturerType.PROFESSOR;
        } else if ("доц.".equals(type)) {
            return LecturerType.DOCENT;
        } else if ("ст. викл.".equals(type)) {
            return LecturerType.SENIORLECTURER;
        } else if ("асист.".equals(type)) {
            return LecturerType.ASSIATANT;
        }
        throw new RuntimeException();
    }

    protected LessonSubgroup mapSubgroup(SubgroupFlag flag){
        switch (flag){
            case FIRST: return LessonSubgroup.FIRST;
            case SECOND: return LessonSubgroup.SECOND;
            case ALL: return LessonSubgroup.BOTH;
            default: return null;
        }
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
}
