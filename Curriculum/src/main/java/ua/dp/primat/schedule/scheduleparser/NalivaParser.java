package ua.dp.primat.schedule.scheduleparser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import edu.dnu.fpm.schedule.parser.ScheduleParser;
import ua.dp.primat.domain.lesson.Lesson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Anton Chernetskij
 */
public class NalivaParser {

    private static final Logger LOGGER = Logger.getLogger(NalivaParser.class.getName());

    private static final String URL = "http://schedule.naliva.com/schedule";

    private LessonMergeStrategy lessonMergeStrategy;

    public void parseAndSave() {
        String schedulePage = getHTML();
        ScheduleParser parser = new ScheduleParser();
        InputStream stream = new ByteArrayInputStream(schedulePage.getBytes());
        ScheduleBuilderImpl scheduleBuilder = new ScheduleBuilderImpl();
        try {
            parser.parse(stream, scheduleBuilder);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing schedule",e);
        }
        List<Lesson> lessons = scheduleBuilder.getLessons();
        lessonMergeStrategy.mergeLessons(lessons);
    }

    protected String getHTML() {
        try {
            LOGGER.info("Loading schedule page from " + URL);
            final WebClient webClient = new WebClient();
            HtmlPage page = null;
            page = webClient.getPage(URL);
            page.executeJavaScript("changeFaculty(6);");
            page = (HtmlPage) page.refresh();

            webClient.closeAllWindows();

            String html = page.asXml();
            LOGGER.info("Received schedule page: \n" + html);
            return html;
        } catch (IOException e) {
            throw new RuntimeException("Error loading schedule html data from " + URL, e);
        }
    }

    public void setLessonMergeStrategy(LessonMergeStrategy lessonMergeStrategy) {
        this.lessonMergeStrategy = lessonMergeStrategy;
    }
}
