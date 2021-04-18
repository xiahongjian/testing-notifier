package tech.hongjian.testingnotifier.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.testingnotifier.entity.JobInfo;
import tech.hongjian.testingnotifier.service.JobService;
import tech.hongjian.testingnotifier.util.R;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Setter(onMethod_ = {@Autowired})
    private JobService jobService;

    @GetMapping("/schedules")
    public R listJob() {
        return R.ok(jobService.listScheduledJob());
    }

    @GetMapping("/infos")
    public R listJobInfo() {
        return R.ok(jobService.listJobInfo());
    }

    @PostMapping("")
    public R createJob(@RequestBody JobInfo jobInfo) {
        jobService.createJob(jobInfo);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R deleteJob(@PathVariable Integer id) {
        jobService.deleteJob(id);
        return R.ok();
    }

    @PutMapping("/{id}/pause")
    public R pauseJob(@PathVariable Integer id) {
        jobService.pauseJob(id);
        return R.ok();
    }

    @PutMapping("/{id}/resume")
    public R resumeJob(@PathVariable Integer id) {
        jobService.resumeJob(id);
        return R.ok();
    }

    @PutMapping("/{id}/reschedule")
    public R rescheduleJob(@PathVariable Integer id, @RequestParam String cron) {
        jobService.rescheduleJob(id, cron);
        return R.ok();
    }

    @PutMapping("/{id}/enable")
    public R enableJob(@PathVariable Integer id) {
        jobService.enableJob(id);
        return R.ok();
    }

    @PutMapping("/{id}/disable")
    public R disableJob(@PathVariable Integer id) {
        jobService.disableJob(id);
        return R.ok();
    }

    @GetMapping("/{id}/do")
    public R doJobRightNow(@PathVariable Integer id) {
        jobService.doJobRightNowOnce(id);
        return R.ok();
    }

}
