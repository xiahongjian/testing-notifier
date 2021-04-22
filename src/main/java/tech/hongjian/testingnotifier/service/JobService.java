package tech.hongjian.testingnotifier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.testingnotifier.entity.JobInfo;
import tech.hongjian.testingnotifier.job.ScheduleJob;
import tech.hongjian.testingnotifier.model.JobInfoVo;
import tech.hongjian.testingnotifier.model.JobState;
import tech.hongjian.testingnotifier.repository.JobInfoRepository;
import tech.hongjian.testingnotifier.util.JSONUtil;
import tech.hongjian.testingnotifier.util.ScheduleUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/4/15.
 */
@Slf4j
@Service
public class JobService {
    @Setter(onMethod_ = {@Autowired})
    private JobInfoRepository jobInfoRepository;

    @Setter(onMethod_ = {@Autowired})
    private Scheduler scheduler;

    @Setter(onMethod_ = {@Autowired})
    private ObjectMapper objectMapper;

    @Setter(onMethod_ = {@Autowired})
    private DictService dictService;

    public List<JobInfo> listAllEnable() {
        return jobInfoRepository.findAllByEnableOrderById(true);
    }

    public JobInfo findById(Integer id) {
        return jobInfoRepository.findById(id).orElse(null);
    }

    public JobInfo exists(Integer id) {
        JobInfo byId = findById(id);
        if (byId == null) {
            throw new ServiceException("ID为" + id + "的任务为找到。");
        }
        return byId;
    }

    public JobInfo createJob(JobInfo jobInfo) {
        validateJobInfo(jobInfo, false);
        JobInfo job = jobInfoRepository.saveAndFlush(jobInfo);

        if (job.getEnable()) {
            startJob(job);
        }
        return job;
    }

    private void validateJobInfo(JobInfo jobInfo, boolean isUpdate) {
        if (jobInfo == null) {
            throw new ServiceException("参数不能为空");
        }
        if (StringUtils.isBlank(jobInfo.getName())) {
            throw new ServiceException("任务名不能为空");
        }
        JobInfo byName = jobInfoRepository.findFirstByName(jobInfo.getName());
        if (!isUpdate && byName != null || isUpdate && !byName.getId().equals(jobInfo.getId())) {
            throw new ServiceException("名称为" + jobInfo.getName() + "的任务已存在");
        }
        if (StringUtils.isBlank(jobInfo.getCron()) && StringUtils.isBlank(jobInfo.getTriggerType())) {
            throw new ServiceException("cron表达式和触发器类型两者至少要存在一个");
        }
    }

    private JobDataMap toJsonDataMap(String json) {
        JavaType type = objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class);
        try {
            return new JobDataMap(objectMapper.readValue(json, type));
        } catch (Exception e) {
            log.warn("解析参数失败：{}， 信息：{}", json, e.getMessage(), e);
        }
        return new JobDataMap();
    }

    public List<JobInfo> listJobInfo() {
        return jobInfoRepository.findAll();
    }

    public void startJob(JobInfo jobInfo) {
        if (!jobInfo.getEnable()) {
            return;
        }
        try {
            JobDetail jobDetail = ScheduleUtil.buildJob(jobInfo);
            String cron = jobInfo.getCron();
            if (StringUtils.isBlank(cron)) {
                cron = dictService.getDictValueByKey(jobInfo.getTriggerType()).getValue();
            }
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(cronScheduleBuilder)
                    .withIdentity(ScheduleUtil.triggerKey(jobInfo))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.warn("创建调度失败，信息：{}", e.getMessage(), e);
        }
    }

    public void doJobRightNowOnce(JobInfo jobInfo) {
        try {
            JobDetail jobDetail = ScheduleUtil.buildJob(jobInfo);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(ScheduleUtil.onceTriggerKey(jobInfo))
                    .startAt(new Date())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(0)) // 3秒后执行，不重复
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.warn("创建调度失败，信息：{}", e.getMessage(), e);
        }
    }

    public void doJobRightNowOnce(Integer jobId) {
        JobInfo jobInfo = exists(jobId);
        doJobRightNowOnce(jobInfo);
    }


    public void pauseJob(Integer jobId) {
        JobInfo byId = exists(jobId);
        try {
            JobKey jobKey = ScheduleUtil.jobKey(byId);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void resumeJob(Integer jobId) {
        JobInfo byId = exists(jobId);
        JobKey jobKey = ScheduleUtil.jobKey(byId);
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Date rescheduleJob(Integer jobId, String cron) {
        JobInfo byId = exists(jobId);
        TriggerKey triggerKey = ScheduleUtil.triggerKey(byId);
        try {
            if (scheduler.checkExists(triggerKey)) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withSchedule(cronScheduleBuilder)
                        .withIdentity(triggerKey)
                        .build();
                return scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteJob(Integer jobId) {
        JobInfo byId = exists(jobId);
        deleteJob(byId);
    }

    public void deleteJob(JobInfo jobInfo) {
        JobKey jobKey = ScheduleUtil.jobKey(jobInfo);
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void updateJob(JobInfo jobInfo) {
        deleteJob(jobInfo.getId());
        startJob(jobInfo);
    }

    public JobInfo getJobInfoByJobKey(JobKey jobKey) {
        String name = jobKey.getName();
        JobInfo jobInfo = jobInfoRepository.findFirstByName(name);
        if (jobInfo == null) {
            jobInfo = new JobInfo();
            jobInfo.setName(jobKey.getName());
            jobInfo.setGroupName(jobKey.getGroup());
        }
        return jobInfo;
    }

    public List<JobInfoVo> listScheduledJob() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyGroup()).stream()
                    .map(jobKey -> getJobInfoByJobKey(jobKey))
                    .filter(Objects::nonNull)
                    .map(jobInfo -> {
                        JobInfoVo vo = new JobInfoVo(jobInfo);
                        Trigger.TriggerState triggerState = null;
                        TriggerKey triggerKey = ScheduleUtil.triggerKey(jobInfo);
                        try {
                            triggerState = scheduler.getTriggerState(triggerKey);
                            JobState state = JobState.of(triggerState.ordinal());
                            vo.setState(state);
                            // 处理临时调度
                            if (jobInfo.getId() == null) {
                                vo.setRemark("临时调度");
                            }
                        } catch (SchedulerException e) {
                            e.printStackTrace();
                        }

                        return vo;
                    }).collect(Collectors.toList());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void enableJob(Integer id) {
        JobInfo jobInfo = exists(id);
        if (jobInfo.getEnable()) {
            return;
        }
        jobInfo.setEnable(Boolean.TRUE);
        jobInfoRepository.save(jobInfo);
        startJob(jobInfo);
    }

    public void disableJob(Integer id) {
        JobInfo jobInfo = exists(id);
        if (!jobInfo.getEnable()) {
            return;
        }
        deleteJob(id);
        jobInfo.setEnable(Boolean.FALSE);
        jobInfoRepository.save(jobInfo);
    }
}
