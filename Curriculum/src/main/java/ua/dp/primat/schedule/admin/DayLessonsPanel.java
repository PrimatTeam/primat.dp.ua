package ua.dp.primat.schedule.admin;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import ua.dp.primat.schedule.services.LessonItem;

/**
 *
 * @author Administrator
 */
public final class DayLessonsPanel extends Panel {
    public DayLessonsPanel(String id, LessonItem[] lessons) {
        super (id);
        add(new Label("day", "sunday"));
        
        List<LessonItem> l = new ArrayList<LessonItem>();
        for (LessonItem les : lessons) {
            l.add(les);
        }
        
        add(new ListView<LessonItem>("lesson", l) {
            int num = 1;
            
            @Override
            protected void populateItem(ListItem<LessonItem> li) {
                LessonItem item = li.getModelObject();
                li.add(new Label("number", "" + num++));
                
                li.add(new EditableScheduleItemPanel("lessonEditPanel", item));
            }
        });
    }
}