package ua.dp.primat.portlet.userinform.app;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import ua.dp.primat.portlet.userinform.services.LiferayUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

public class HomePage extends WebPage {

    /**
     * Constructor for the page, which takes user id from liferay's url.
     *
     * Link example: liferay.com/web/myuser
     *
     * @param parameters - page parameters
     */
    public HomePage(final PageParameters parameters) {
        super(parameters);

        final Request req = RequestCycle.get().getRequest();
        final HttpServletRequest httpreq = ((ServletWebRequest)req).getHttpServletRequest();

        final User lrUser = liferayUserService.getUserInfo(httpreq);
        if (lrUser == null) {
            add(new Label(UD, bundle.getString("label.no.user")));
            add(new Label("page.header", bundle.getString("page.header.user")));
        } else {
            if (isLecturer(lrUser)){
                add(new LecturerInfoPanel(UD, lrUser));
                add(new Label("page.header", bundle.getString("page.header.lecturer")));
            } else {
                add(new Label("page.header", bundle.getString("page.header.user")));
                add(new UserInfoPanel(UD, lrUser));
            }
        }

    }

    private boolean isLecturer(User user) {
        try {
            for(Role role: user.getRoles()){
                if (role.getDescriptiveName().equals("Lecturer")){
                    return true;
                }
            }
            return false;
        } catch (SystemException e) {
            throw new RuntimeException(e);
        } catch (PortalException e) {
            throw new RuntimeException(e);
        }
    }

    private final ResourceBundle bundle = ResourceBundle.getBundle(
            "ua.dp.primat.portlet.userinform.app.HomePage");

    @SpringBean
    private LiferayUserService liferayUserService;

    private static final long serialVersionUID = 1L;
    private static final String UD = "userDetails";
}
