package ua.dp.primat.schedule.scheduleparser;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.dp.primat.domain.lesson.Lesson;
import ua.dp.primat.repositories.LecturerRepository;
import ua.dp.primat.repositories.LessonRepository;
import ua.dp.primat.repositories.StudentGroupRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Chernetskij
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class NalivaParserTest {

    @Autowired
    private NalivaParser parser;

    @Autowired
    private LessonRepository repository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Test
    public void testGetHTML() throws Exception {
        String page = parser.getHTML();
        Pattern pattern = Pattern.compile("<tr>");
        Matcher matcher = pattern.matcher(page);
        int count = 0;
        while (matcher.find()){
            count ++;
        }
        Assert.assertTrue("Empty table", count > 4);
        System.out.println(page);
    }

    @Test
    public void testParse(){
        final LessonMergeStrategy lessonMergeStrategy = parser.getLessonMergeStrategy();
        parser = new NalivaParser(){
            @Override
            protected InputStream getPageAsStream() {
                try {
                    return new FileInputStream("src/test/resources/schedule.html");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        parser.setLessonMergeStrategy(lessonMergeStrategy);

        parser.parseAndSave();

        final List<Lesson> allLessons = repository.getAllLessons();
        System.out.println(allLessons);
    }
}