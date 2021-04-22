package tech.hongjian.testingnotifier.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import tech.hongjian.testingnotifier.util.ScheduleUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by xiahongjian on 2021/4/15.
 */
@Slf4j
@Component
public class PrintTimeJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String param = ScheduleUtil.getJobStringParam(jobExecutionContext, "param");
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ": " + param);
    }
}
