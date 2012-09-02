package ua.dp.primat.schedule.scheduleparser;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anton Chernetskij
 */
public class NalivaParserTest {

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
}
