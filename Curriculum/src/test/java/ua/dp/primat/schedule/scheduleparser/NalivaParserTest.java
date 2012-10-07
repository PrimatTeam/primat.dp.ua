package ua.dp.primat.schedule.scheduleparser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Chernetskij
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class NalivaParserTest {

    @Autowired
    private NalivaParser parser;

    @Test
    public void testGetHTML() throws Exception {
        NalivaParser nalivaParser = new NalivaParser();
        String page = nalivaParser.getHTML();
        Pattern pattern = Pattern.compile("<tr>");
        Matcher matcher = pattern.matcher(page);
        int count = 0;
        while (matcher.find()){
            count ++;
        }
        Assert.assertTrue("Empty table", count > 4);
    }

    @Test
    public void testParseAndSave(){
        parser.parseAndSave();
    }
}
