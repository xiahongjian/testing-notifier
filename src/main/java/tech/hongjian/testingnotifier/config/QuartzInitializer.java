package tech.hongjian.testingnotifier.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import tech.hongjian.testingnotifier.entity.JobInfo;
import tech.hongjian.testingnotifier.service.JobService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by xiahongjian on 2021/4/15.
 */
@Configuration
public class QuartzInitializer {
    @Setter(onMethod_ = {@Autowired})
    private JobService jobService;

    @PostConstruct
    public void init() {
        List<JobInfo> jobInfos = jobService.listAllEnable();
        for (JobInfo jobInfo : jobInfos) {
            jobService.startJob(jobInfo);
        }
    }

}
