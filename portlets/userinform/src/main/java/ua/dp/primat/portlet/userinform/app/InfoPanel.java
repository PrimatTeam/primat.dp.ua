package ua.dp.primat.portlet.userinform.app;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.ResourceBundle;

public class InfoPanel extends Panel {
    protected static final String COMA = ", ";
    protected static final String MINUS = "-";

    public InfoPanel(String id) {
        super(id);
    }

    protected String getAvatarPath(User user) {
        return String.format("/image/user_%s_portrait?img_id=%d", "male",
                             user.getPortraitId());
    }

    protected String getUserOrganization(User user) {
        try {
            final StringBuilder groups = new StringBuilder();
            for (Organization o : user.getOrganizations()) {
                if (groups.length() > 0) {
                    groups.append(COMA);
                }
                groups.append(o.getName());
            }
            return groups.toString();
        } catch (SystemException se) {
            return MINUS;
        } catch (PortalException pe) {
            return MINUS;
        }
    }
}
