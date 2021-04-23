package tech.hongjian.testingnotifier.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import tech.hongjian.testingnotifier.entity.JobInfo;
import tech.hongjian.testingnotifier.job.ScheduleJob;

import java.util.Collections;
import java.util.Map;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@Slf4j
public class ScheduleUtil {
    public static final String TRIGGER_GROUP_ONCE = "once";
    public static final String VARIABLE_KEY_JOB = "__job_key__";
    public static final String VARIABLE_KEY_PARAMS = "__params__";


    public static JobKey jobKey(JobInfo jobInfo) {
        return JobKey.jobKey(jobInfo.getName(), jobInfo.getGroupName());
    }

    public static JobKey jobKey(JobInfo jobInfo, boolean onlyOnce) {
        return JobKey.jobKey(jobInfo.getName(), TRIGGER_GROUP_ONCE);
    }

    public static TriggerKey triggerKey(JobInfo jobInfo) {
        return TriggerKey.triggerKey(jobInfo.getName(), jobInfo.getGroupName());
    }

    public static TriggerKey onceTriggerKey(JobInfo jobInfo) {
        return TriggerKey.triggerKey(jobInfo.getName(), TRIGGER_GROUP_ONCE);
    }

    public static Object getJobParam(JobExecutionContext context, String paramName) {
        Map<String, Object> jobParamMap = getJobParamMap(context);
        return jobParamMap.get(paramName);
    }

    public static String getJobStringParam(JobExecutionContext context, String paramName) {
        return (String) getJobParam(context, paramName);
    }

    public static Class<? extends Job> getJobClass(JobExecutionContext context) {
        return getJobClass(context.getMergedJobDataMap());
    }

    public static Class<? extends Job> getJobClass(JobDataMap jobDataMap) {
        String className = jobDataMap.getString(VARIABLE_KEY_JOB);
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(className);
            return jobClass;
        } catch (ClassNotFoundException e) {
            log.error("未找到class：{}，信息：{}", e.getMessage(), e);
        }
        return null;
    }

    public static JobDataMap setJobClass(JobDataMap jobDataMap, String jobClassName) {
        jobDataMap.put(VARIABLE_KEY_JOB, jobClassName);
        return jobDataMap;
    }

    public static Map<String, Object> getJobParamMap(JobExecutionContext context) {
        return getJobParamMap(context.getMergedJobDataMap());
    }

    public static Map<String, Object> getJobParamMap(JobDataMap jobDataMap) {
        return (Map<String, Object>) jobDataMap.get(VARIABLE_KEY_PARAMS);
    }

    public static JobDataMap setJobParamMap(JobDataMap jobDataMap, Map<String, Object> paramMap) {
        jobDataMap.put(VARIABLE_KEY_PARAMS, paramMap);
        return jobDataMap;
    }

    public static JobDetail buildJob(JobInfo jobInfo) {
        return buildJob(jobInfo, false);
    }

    public static JobDetail buildJob(JobInfo jobInfo, boolean onlyOnce) {
        JobKey jobKey = onlyOnce ? jobKey(jobInfo, true) : jobKey(jobInfo);
        JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(jobKey)
                .build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        ScheduleUtil.setJobClass(jobDataMap, jobInfo.getClassName());
        String params = jobInfo.getParams();
        ScheduleUtil.setJobParamMap(jobDataMap, StringUtils.isBlank(params) ? Collections.emptyMap() : JSONUtil.toMap(params, Object.class));
        return jobDetail;
    }
}
