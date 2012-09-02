package ua.dp.primat.schedule.scheduleparser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.logging.Logger;

/**
 * Created by Anton Chernetskij
 */
public class NalivaParser {
    private static final Logger LOGGER = Logger.getLogger(NalivaParser.class.getName());
    private static final String URL = "http://schedule.naliva.com/schedule";

    protected String getHTML() throws Exception {
        LOGGER.info("Getting schedule page from " + URL);
        final WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage(URL);

        page.executeJavaScript("changeFaculty(6);");
        page = (HtmlPage) page.refresh();

        webClient.closeAllWindows();

        String html = page.asXml();
        LOGGER.info("Received schedule page: \n" + html);

        return html;
    }
}
