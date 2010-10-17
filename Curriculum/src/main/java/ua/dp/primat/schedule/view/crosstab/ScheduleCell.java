package ua.dp.primat.schedule.view.crosstab;

import ua.dp.primat.domain.lesson.Lesson;
import org.apache.wicket.markup.html.basic.Label;
import ua.dp.primat.schedule.view.ShedulePanel;

/**
 * Panel, that outputs one schedule cell for specified Lesson.
 * It outputs the subject name, lecturer etc.
 * @author fdevelop
 */
public final class ScheduleCell extends ShedulePanel {

    public ScheduleCell(final String id, Lesson lesson) {
        super(id);

        final String cellSubject = ((lesson == null) ? "" : lesson.getLessonDescription().getDiscipline().getName());
        final String cellType = ((lesson == null) ? "" : "(" + lesson.getLessonDescription().getLessonType() + ")");
        final String cellLecturer = ((lesson == null) ? "" : lesson.getLessonDescription().getLecturerNames());
        final String cellGroup = ((lesson == null) || (lesson.getLessonDescription() == null))
                ? "" : lesson.getLessonDescription().getStudentGroup().toString();
        final String cellRoom = (((lesson == null) || (lesson.getRoom() == null)) ? "" : lesson.getRoom().toString());

        add(new Label("cellSubject", cellSubject));
        add(new Label("cellType", cellType));
        add(new LecturerLabel("cellLecturer", cellLecturer));
        add(new GroupLabel("cellGroup", cellGroup));
        add(new RoomLabel("cellRoom", cellRoom));
    }

    private static final long serialVersionUID = 1L;

    private class LecturerLabel extends Label {

        public LecturerLabel(String id, String label) {
            super(id, label);
        }

        @Override
        public boolean isVisible() {
            return isLecturerVisible();
        }
        private static final long serialVersionUID = 1L;
    }

    private class GroupLabel extends Label {

        public GroupLabel(String id, String label) {
            super(id, label);
        }

        @Override
        public boolean isVisible() {
            return isGroupVisible();
        }
        private static final long serialVersionUID = 1L;
    }

    private class RoomLabel extends Label {

        public RoomLabel(String id, String label) {
            super(id, label);
        }

        @Override
        public boolean isVisible() {
            return isRoomVisible();
        }
        private static final long serialVersionUID = 1L;
    }
}
