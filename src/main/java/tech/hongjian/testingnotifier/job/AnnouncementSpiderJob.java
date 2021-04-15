package tech.hongjian.testingnotifier.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.hongjian.testingnotifier.service.NotificationService;
import tech.hongjian.testingnotifier.parser.AnnouncementParser;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@Transactional
@Slf4j
@Component
public class AnnouncementSpiderJob implements Job {
    @Setter(onMethod_ = {@Autowired})
    private EntityManager em;

    @Setter(onMethod_ = {@Autowired})
    private AnnouncementParser parser;

    @Setter(onMethod_ = {@Autowired})
    private NotificationService notificationService;

    public static final String VAR_INTERVAL = "interval";

    public void doParse(int interval) {
        log.info("开始爬取通知...");

        // 获取最新的报名公告ID
        List<Integer> list = em.createQuery("select announcementId from Announcement order by announcementId desc", Integer.class)
                .setMaxResults(1)
                .getResultList();
        // 爬取报名公告
        parser.parse(list.isEmpty() ? null : list.get(0), interval);

        // 发送通知
        notificationService.sendNotification();
        log.info("爬取通知结束。");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        int interval = 5;
        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        if (mergedJobDataMap.containsKey(VAR_INTERVAL)) {
            interval = mergedJobDataMap.getInt(VAR_INTERVAL);
        }
        doParse(interval);
    }
}
