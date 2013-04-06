package ua.dp.primat.schedule.scheduleparser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import edu.dnu.fpm.schedule.parser.ScheduleParser;
import org.springframework.beans.factory.annotation.Autowired;
import ua.dp.primat.domain.lesson.Lesson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Anton Chernetskij
 */
public class NalivaParser {

    private static final Log LOGGER = LogFactoryUtil.getLog(NalivaParser.class);

    private String URL;

    @Autowired
    private LessonMergeStrategy lessonMergeStrategy;

    public void parseAndSave() {
        ScheduleParser parser = new ScheduleParser();
        ScheduleBuilderImpl scheduleBuilder = new ScheduleBuilderImpl();
        InputStream stream = getPageAsStream();
        try {
            parser.parse(stream, scheduleBuilder);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing schedule",e);
        }
        List<Lesson> lessons = scheduleBuilder.getLessons();
        lessonMergeStrategy.mergeLessons(lessons);
        LOGGER.info("Saving finished");
    }

    protected synchronized String getHTML() {
        try {
            LOGGER.info("Loading schedule page from " + URL);
            final WebClient webClient = new WebClient();
            HtmlPage page = webClient.getPage(URL);

            giveNalivaSomeRest();
            page.executeJavaScript("changeFaculty(6);");
            giveNalivaSomeRest();

            page = (HtmlPage) page.refresh();

            webClient.closeAllWindows();
            LOGGER.info("Loading finished.");

            String html = page.asXml();
//            LOGGER.debug("Received schedule page: \n" + html);
            return html;
        } catch (IOException e) {
            throw new RuntimeException("Error loading schedule html data from " + URL, e);
        }
    }

    private synchronized void giveNalivaSomeRest(){
        try {
            wait(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected InputStream getPageAsStream(){
        String schedulePage = getHTML();
        return new ByteArrayInputStream(schedulePage.getBytes());
    }

    public void setLessonMergeStrategy(LessonMergeStrategy lessonMergeStrategy) {
        this.lessonMergeStrategy = lessonMergeStrategy;
    }

    public LessonMergeStrategy getLessonMergeStrategy() {
        return lessonMergeStrategy;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
