package ua.dp.primat.portlet.userinform.app;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;

import java.text.DateFormat;
import java.util.ResourceBundle;

/**
 *
 * @author fdevelop
 */
public class UserInfoPanel extends InfoPanel {

    public UserInfoPanel(String id, User user) {
        super(id);
        add(new Label("fullname", user.getFullName()));
        add(new Label("sex", getSex(user)));
        add(new Label("email", user.getDisplayEmailAddress()));
        add(new Label("screenname", user.getScreenName()));
        add(new Label("birthday", getUserBirthday(user)));
        add(new Label("school", getUserSchool(user)));
        add(new Label("role", getUserRoles(user)));
        add(new Label("group", getUserGroups(user)));
        add(new Label("cath", getUserOrganization(user)));

        final Image ava = new Image("avatar");
        ava.add(new SimpleAttributeModifier("src", getAvatarPath(user)));
        add(ava);
    }

    private String getSex(User user) {
        try {
            return user.isMale() ? bundle.getString("sex.male")
                    : bundle.getString("sex.female");
        } catch (SystemException se) {
            return MINUS;
        } catch (PortalException pe) {
            return MINUS;
        }
    }

    private String getUserSchool(User user) {
        if (user.getExpandoBridge() == null) {
            return MINUS;
        }
        final String school = user.getExpandoBridge().getAttribute("school-school").toString();
        final String sCountry = user.getExpandoBridge().getAttribute("school-country").toString();
        final String sCity = user.getExpandoBridge().getAttribute("school-city").toString();
        return (school.length() > 0) ? String.format("%s (%s, %s)", school, sCity, sCountry) : MINUS;
    }

    private String getUserGroups(User user) {
        try {
            final StringBuilder groups = new StringBuilder();
            for (Group t : user.getGroups()) {
                if (groups.length() > 0) {
                    groups.append(COMA);
                }
                groups.append(t.getName());
            }
            return groups.toString();
        } catch (SystemException se) {
            return MINUS;
        } catch (PortalException pe) {
            return MINUS;
        }
    }

    private String getUserRoles(User user) {
        try {
            final StringBuilder groups = new StringBuilder();

            for (Role r : user.getRoles()) {
                if (groups.length() > 0) {
                    groups.append(COMA);
                }
                groups.append(r.getTitle("uk", true));
            }
            return groups.toString();
        } catch (SystemException se) {
            return MINUS;
        }
    }

    private String getUserBirthday(User user) {
        try {
            return DateFormat.getDateInstance(DateFormat.MEDIUM).format(user.getBirthday());
        } catch (PortalException pe) {
            return MINUS;
        } catch (SystemException se) {
            return MINUS;
        }
    }

    private static final long serialVersionUID = 1L;
    protected final ResourceBundle bundle = ResourceBundle.getBundle(
            "ua.dp.primat.portlet.userinform.app.UserInfoPanel");
}
