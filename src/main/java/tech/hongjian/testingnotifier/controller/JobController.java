package tech.hongjian.testingnotifier.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.testingnotifier.entity.JobInfo;
import tech.hongjian.testingnotifier.service.JobInfoService;
import tech.hongjian.testingnotifier.util.R;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@RestController
@RequestMapping("/jobs")
public class JobController {
    @Setter(onMethod_ = {@Autowired})
    private JobInfoService jobInfoService;

    @GetMapping("")
    public R listJob() {
        return R.ok(jobInfoService.listScheduledJob());
    }

    @PostMapping("")
    public R createJob(@RequestBody JobInfo jobInfo) {
        jobInfoService.createJob(jobInfo);
        return R.ok();
    }

    @PutMapping("/{id}/pause")
    public R pauseJob(@PathVariable Integer id) {
        jobInfoService.pauseJob(id);
        return R.ok();
    }

    @PutMapping("/{id}/resume")
    public R resumeJob(@PathVariable Integer id) {
        jobInfoService.resumeJob(id);
        return R.ok();
    }

    @PutMapping("/{id}/reschedule")
    public R rescheduleJob(@PathVariable Integer id, @RequestParam String cron) {
        jobInfoService.rescheduleJob(id, cron);
        return R.ok();
    }

    @PutMapping("/{id}/delete")
    public R deleteJob(@PathVariable Integer id) {
        jobInfoService.deleteJob(id);
        return R.ok();
    }
}
