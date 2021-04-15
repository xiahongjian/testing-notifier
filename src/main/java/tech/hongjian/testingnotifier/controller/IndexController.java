package tech.hongjian.testingnotifier.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.testingnotifier.entity.JobInfo;
import tech.hongjian.testingnotifier.parser.AnnouncementParser;
import tech.hongjian.testingnotifier.service.JobInfoService;
import tech.hongjian.testingnotifier.service.NotificationService;
import tech.hongjian.testingnotifier.job.AnnouncementSpiderJob;
import tech.hongjian.testingnotifier.util.R;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@RestController
public class IndexController {
    @Setter(onMethod_ = {@Autowired})
    private JobInfoService jobInfoService;

    @PostMapping("/jobs")
    public R createJob(@RequestBody JobInfo jobInfo) {
        jobInfoService.createJob(jobInfo);
        return R.ok();
    }

    @PutMapping("/jobs/{id}/pause")
    public R pauseJob(@PathVariable Integer id) {
        jobInfoService.pauseJob(id);
        return R.ok();
    }

    @PutMapping("/jobs/{id}/resume")
    public R resumeJob(@PathVariable Integer id) {
        jobInfoService.resumeJob(id);
        return R.ok();
    }

    @PutMapping("/jobs/{id}/reschedule")
    public R rescheduleJob(@PathVariable Integer id, @RequestParam String cron) {
        jobInfoService.rescheduleJob(id, cron);
        return R.ok();
    }

    @PutMapping("/jobs/{id}/delete")
    public R deleteJob(@PathVariable Integer id) {
        jobInfoService.deleteJob(id);
        return R.ok();
    }

}
