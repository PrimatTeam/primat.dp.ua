package ua.dp.primat.portlet.userinform.app;

import com.liferay.portal.model.User;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *
 * @author pesua
 */
public class LecturerInfoPanel extends InfoPanel {

    public LecturerInfoPanel(String id, final User user) {
        super(id);
        String fullName = user.getFullName();
        List<String> names = new ArrayList<String>(Arrays.asList(
                fullName,
                (String) user.getExpandoBridge().getAttribute(LecturerCustomField.Position.name()),
                (String) user.getExpandoBridge().getAttribute(LecturerCustomField.Degree.name()),
                (String) user.getExpandoBridge().getAttribute(LecturerCustomField.Title.name())
        ));
        names.removeAll(Arrays.asList("", null));
        add(new Label("fullname", StringUtils.collectionToDelimitedString(names, COMA)));
        add(new Label("email", user.getDisplayEmailAddress()));
        add(new Label("cath", getUserOrganization(user)));

        List<LecturerCustomField> fields = new ArrayList<LecturerCustomField>(Arrays.asList(LecturerCustomField.values()));
        fields.remove(LecturerCustomField.Position);
        fields.remove(LecturerCustomField.Degree);
        fields.remove(LecturerCustomField.Title);
        ListView listview = new ListView<LecturerCustomField>("fields", fields) {
            protected void populateItem(ListItem item) {
                String name = item.getModelObject().toString();

                item.add(new Label("label", bundle.getString("label." + name)));

                String value = (String) user.getExpandoBridge().getAttribute(name);
                item.add(new Label("value", value));
            }
        };
        add(listview);

        final Image ava = new Image("avatar");
        ava.add(new SimpleAttributeModifier("src", getAvatarPath(user)));
        add(ava);
    }

    private static final long serialVersionUID = 1L;
    protected final ResourceBundle bundle = ResourceBundle.getBundle(
            "ua.dp.primat.portlet.userinform.app.LecturerInfoPanel");
}
