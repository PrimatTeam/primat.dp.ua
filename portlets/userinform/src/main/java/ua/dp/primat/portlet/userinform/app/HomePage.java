package ua.dp.primat.portlet.userinform.app;

import com.liferay.portal.PortalException;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.liferay.portal.model.User;
import ua.dp.primat.portlet.userinform.services.LiferayUserService;

/**
 * Homepage
 */
public class HomePage extends WebPage {

    /**
     * Constructor for the page, which takes user id and info from
     * page parameter or shows no-user-page.
     *
     * Link example: http://localhost/.../pagename?p_p_id
     *      =userinform_WAR_userinform&userId=11331
     *
     * @param parameters - page parameters
     */
    public HomePage(final PageParameters parameters) {
        Long userId = Long.valueOf(parameters.getAsLong("userId", -1));
        User lrUser = userService.getUserInfo(userId);
        if (lrUser == null) {
            add(new Label("userDetails", "No user was selected"));
        } else {
            add(new UserInfoPanel("userDetails", lrUser));
        }

    }

    @SpringBean
    private LiferayUserService userService;

    private static final long serialVersionUID = 1L;
}
