package tech.hongjian.testingnotifier.util;

import org.quartz.JobKey;
import org.quartz.TriggerKey;
import tech.hongjian.testingnotifier.entity.JobInfo;

/**
 * Created by xiahongjian on 2021/4/17.
 */
public class ScheduleUtil {
    public static JobKey jobKey(JobInfo jobInfo) {
        return JobKey.jobKey(jobInfo.getName(), jobInfo.getGroup());
    }

    private static TriggerKey triggerKey(JobInfo jobInfo) {
        return TriggerKey.triggerKey(jobInfo.getName(), jobInfo.getGroup());
    }
}
