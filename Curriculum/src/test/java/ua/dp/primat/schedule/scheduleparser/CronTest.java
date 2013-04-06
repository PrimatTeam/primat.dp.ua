package ua.dp.primat.schedule.scheduleparser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.GregorianCalendar;

/**
 * Created by Anton Chernetskij
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CronTest {
    @Autowired
    CronTriggerBean cronTrigger;

    @Test
    public void test() throws Exception {
        String cronExpression = cronTrigger.getCronExpression();
        CronExpression expression = new CronExpression(cronExpression);
        Assert.assertTrue(expression.isSatisfiedBy(new GregorianCalendar(2013, 8, 1, 3, 0, 0).getTime()));
        Assert.assertTrue(expression.isSatisfiedBy(new GregorianCalendar(2013, 9, 3, 3, 0, 0).getTime()));
        Assert.assertTrue(expression.isSatisfiedBy(new GregorianCalendar(2013, 0, 1, 3, 0, 0).getTime()));
        Assert.assertTrue(expression.isSatisfiedBy(new GregorianCalendar(2013, 1, 3, 3, 0, 0).getTime()));
        
        Assert.assertFalse(expression.isSatisfiedBy(new GregorianCalendar(2013, 8, 2, 3, 0, 0).getTime()));
        Assert.assertFalse(expression.isSatisfiedBy(new GregorianCalendar(2013, 9, 4, 3, 0, 0).getTime()));
        Assert.assertFalse(expression.isSatisfiedBy(new GregorianCalendar(2013, 0, 1, 4, 0, 0).getTime()));
        Assert.assertFalse(expression.isSatisfiedBy(new GregorianCalendar(2013, 3, 1, 3, 0, 0).getTime()));
    }
}
