package tech.hongjian.testingnotifier.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import tech.hongjian.testingnotifier.service.ScheduleJobLogService;
import tech.hongjian.testingnotifier.util.ApplicationContextUtil;
import tech.hongjian.testingnotifier.util.JSONUtil;
import tech.hongjian.testingnotifier.util.ScheduleUtil;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by xiahongjian on 2021/4/22.
 */
@Slf4j
public class ScheduleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Class<? extends Job> jobClass = ScheduleUtil.getJobClass(context);
        Job job = ApplicationContextUtil.getBean(jobClass);
        LocalDateTime startAt = LocalDateTime.now();
        boolean success = true;
        try {
            job.execute(context);
        } catch (Exception e) {
            log.error("执行调度失败，信息：{}", e.getMessage(), e);
            success = false;
        }
        LocalDateTime finishAt = LocalDateTime.now();
        Map<String, Object> paramMap = ScheduleUtil.getJobParamMap(context);
        ApplicationContextUtil.getBean(ScheduleJobLogService.class).createLog(jobClass.getName(), JSONUtil.toJSON(paramMap), success, startAt, finishAt);
    }
}
