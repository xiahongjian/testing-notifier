package tech.hongjian.testingnotifier.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.testingnotifier.parser.AnnouncementParser;
import tech.hongjian.testingnotifier.service.NotificationService;
import tech.hongjian.testingnotifier.task.AnnouncementSpiderTask;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@RestController
public class IndexController {
    @Setter(onMethod_ = {@Autowired})
    private AnnouncementParser parser;

    @Setter(onMethod_ = {@Autowired})
    private NotificationService notificationService;

    @Setter(onMethod_ = {@Autowired})
    private AnnouncementSpiderTask task;


    @GetMapping("/parse")

    public String parse() {
        parser.parse(null);
        return "done";
    }

    @GetMapping("/notify")
    public String sendNotification() {
        notificationService.sendNotification();
        return "done";
    }

    @GetMapping("/task")
    public String doTask() {
        task.doParse();
        return "done";
    }
}
