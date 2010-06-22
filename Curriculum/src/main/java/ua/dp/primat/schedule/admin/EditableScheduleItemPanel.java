package ua.dp.primat.schedule.admin;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import ua.dp.primat.schedule.services.LessonItem;

/**
 *
 * @author EniSh
 */
public final class EditableScheduleItemPanel extends Panel {
    public EditableScheduleItemPanel(String id, LessonItem li) {
        super (id);
        add(new AjaxCheckBox("singleLesson") {

            @Override
            protected void onUpdate(AjaxRequestTarget art) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        add(new EditableLesonPanel("numerator", li.getNumerator()));
        EditableLesonPanel elp = new EditableLesonPanel("denominator", li.getDenominator());
        elp.setVisible(!li.isOneLesson());
        add(elp);
    }
}
