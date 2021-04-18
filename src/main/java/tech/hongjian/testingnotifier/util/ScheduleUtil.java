package tech.hongjian.testingnotifier.util;

import org.quartz.JobKey;
import org.quartz.TriggerKey;
import tech.hongjian.testingnotifier.entity.JobInfo;

/**
 * Created by xiahongjian on 2021/4/17.
 */
public class ScheduleUtil {
    public static final String TRIGGER_GROUP_ONCE = "once";

    public static JobKey jobKey(JobInfo jobInfo) {
        return JobKey.jobKey(jobInfo.getName(), jobInfo.getGroupName());
    }

    public static TriggerKey triggerKey(JobInfo jobInfo) {
        return TriggerKey.triggerKey(jobInfo.getName(), jobInfo.getGroupName());
    }

    public static TriggerKey onceTriggerKey(JobInfo jobInfo) {
        return TriggerKey.triggerKey(jobInfo.getName(), TRIGGER_GROUP_ONCE);
    }
}
