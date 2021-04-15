package tech.hongjian.testingnotifier.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.testingnotifier.entity.JobInfo;
import tech.hongjian.testingnotifier.repository.JobInfoRepository;

import java.util.*;

/**
 * Created by xiahongjian on 2021/4/15.
 */
@Service
public class JobInfoService {
    @Setter(onMethod_ = {@Autowired})
    private JobInfoRepository jobInfoRepository;

    @Setter(onMethod_ = {@Autowired})
    private Scheduler scheduler;

    @Setter(onMethod_ = {@Autowired})
    private ObjectMapper objectMapper;

    public List<JobInfo> listAllEnable() {
        return jobInfoRepository.findAllByEnableOrderById(true);
    }

    public JobInfo findById(Integer id) {
        return jobInfoRepository.findById(id).orElse(null);
    }

    public JobInfo createJob(JobInfo jobInfo) {
        if (jobInfo == null) {
            throw new ServiceException("参数不能为空");
        }
        if (StringUtils.isBlank(jobInfo.getName())) {
            throw new ServiceException("任务名不能为空");
        }
        if (jobInfoRepository.countByName(jobInfo.getName()) > 0) {
            throw new ServiceException("任务名称不能重复");
        }
        JobInfo job = jobInfoRepository.saveAndFlush(jobInfo);

        if (job.getEnable()) {
            // 启动job
        }
        return job;
    }

    private Map<String, Object> toMap(String json) {
        JavaType type = objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class);
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public void startJob(JobInfo jobInfo) {
        if (!jobInfo.getEnable()) {
            return;
        }
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobInfo.getClassName());

            JobDataMap jobDataMap = new JobDataMap(toMap(jobInfo.getParams()));
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(JobKey.jobKey(jobInfo.getName()))
                    .setJobData(jobDataMap)
                    .build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCron());
            Trigger trigger = TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).withIdentity(TriggerKey
                    .triggerKey(jobInfo.getName()))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    public void pauseJob(Integer jobId) {
        JobInfo byId = findById(jobId);
        if (byId == null) {
            return;
        }
        try {
            JobKey jobKey = JobKey.jobKey(byId.getName());
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void resumeJob(Integer jobId) {
        JobInfo byId = findById(jobId);
        if (byId == null) {
            return;
        }
        JobKey jobKey = JobKey.jobKey(byId.getName());
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Date rescheduleJob(Integer jobId) {
        JobInfo byId = findById(jobId);
        if (byId == null) {
            return null;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(byId.getName());
        try {
            if (scheduler.checkExists(triggerKey)) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(byId.getCron());
                Trigger trigger = TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).withIdentity(triggerKey).build();
                return scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteJob(Integer jobId) {
        JobInfo byId = findById(jobId);
        if (byId == null) {
            return;
        }
        JobKey jobKey = JobKey.jobKey(byId.getName());
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void updateJobParam(JobInfo jobInfo) {
        deleteJob(jobInfo.getId());
        startJob(jobInfo);
    }
}
