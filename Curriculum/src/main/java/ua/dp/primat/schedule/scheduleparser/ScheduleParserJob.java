package ua.dp.primat.schedule.scheduleparser;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by Anton Chernetskij
 */
public class ScheduleParserJob extends QuartzJobBean {

    private static final Log LOGGER = LogFactoryUtil.getLog(ScheduleParserJob.class);

    @Autowired
    NalivaParser parser;

    @Override
    protected void executeInternal(org.quartz.JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Schedule parsing job triggered.");
        parser.parseAndSave();
    }

    public void setParser(NalivaParser parser) {
        this.parser = parser;
    }
}
