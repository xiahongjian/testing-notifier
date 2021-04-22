package tech.hongjian.testingnotifier.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.testingnotifier.entity.ScheduleJobLog;
import tech.hongjian.testingnotifier.service.ScheduleJobLogService;
import tech.hongjian.testingnotifier.util.R;

/**
 * Created by xiahongjian on 2021/4/22.
 */
@RequestMapping("/api/logs")
@RestController
public class ScheduleJobLogController {
    @Setter(onMethod_ = {@Autowired})
    private ScheduleJobLogService scheduleJobLogService;

    @GetMapping("")
    public R list(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        Page<ScheduleJobLog> scheduleJobLogs = scheduleJobLogService.listPage(pageNumber - 1, pageSize);
        return R.ok(scheduleJobLogs.getContent()).total((int) scheduleJobLogs.getTotalElements());
    }
}
