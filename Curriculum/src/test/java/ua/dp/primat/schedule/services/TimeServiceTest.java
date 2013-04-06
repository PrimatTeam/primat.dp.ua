package ua.dp.primat.schedule.services;

import junit.framework.Assert;
import org.junit.Test;
import ua.dp.primat.domain.lesson.WeekType;

import java.util.GregorianCalendar;

/**
 * Created by Anton Chernetskij
 */
public class TimeServiceTest {
    @Test
    public void testWeekTypeByDate() throws Exception {
        TimeService service = new TimeService();
        Assert.assertEquals(WeekType.NUMERATOR, service.weekTypeByDate(new GregorianCalendar(2012,8,1).getTime()));
        Assert.assertEquals(WeekType.NUMERATOR, service.weekTypeByDate(new GregorianCalendar(2012,8,2).getTime()));
        Assert.assertEquals(WeekType.NUMERATOR, service.weekTypeByDate(new GregorianCalendar(2012,8,3).getTime()));
    }
}
