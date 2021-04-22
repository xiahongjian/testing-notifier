package tech.hongjian.testingnotifier.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.hongjian.testingnotifier.entity.ScheduleJobLog;
import tech.hongjian.testingnotifier.repository.ScheduleJobLogRepository;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by xiahongjian on 2021/4/22.
 */
@Service
public class ScheduleJobLogService {
    @Setter(onMethod_ = {@Autowired})
    private ScheduleJobLogRepository scheduleJobLogRepository;

    public ScheduleJobLog createLog(String jobClassName, String params, boolean success, LocalDateTime startAt, LocalDateTime finishAt) {
        ScheduleJobLog log = new ScheduleJobLog();
        log.setJobClass(jobClassName);
        log.setParams(params);
        log.setSuccess(success);
        log.setStartAt(startAt);
        log.setFinishAt(finishAt);
        log.setDuration(Duration.between(startAt, finishAt).toMillis());
        return scheduleJobLogRepository.save(log);
    }

    public Page<ScheduleJobLog> listPage(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.Direction.DESC, "createdAt");
        return scheduleJobLogRepository.findAll(pageRequest);
    }
}
