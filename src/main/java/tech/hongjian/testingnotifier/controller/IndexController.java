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


}
