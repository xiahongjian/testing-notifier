package tech.hongjian.testingnotifier.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hongjian.testingnotifier.entity.Announcement;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@Transactional
@Service
public class NotificationService {
    @Setter(onMethod_ = {@Autowired})
    private EntityManager em;

    @Setter(onMethod_ = {@Autowired})
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:hongjian.xia@qq.com}")
    private String from;

    @Value("${notify.tos}")
    private String[] tos;


    public static final String NOTIFICATION_SUBJECT = "普通话考试报名通知";

    public void sendNotification() {
        List<Announcement> announcements = allNeedToSendNotification();
        if (announcements.isEmpty()) {
            return;
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(tos);
        mail.setSubject(NOTIFICATION_SUBJECT);
        mail.setText(generateMailContent(announcements));
        mailSender.send(mail);

        // 更新状态
        for (Announcement announcement : announcements) {
            announcement.setHasNotified(true);
            em.merge(announcement);
        }
    }

    private String generateMailContent(List<Announcement> announcements) {
        StringBuilder sb = new StringBuilder(256);
        boolean isFirst = true;
        for (Announcement announcement : announcements) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("==========================================\n");
            }
            sb.append("标题：");
            sb.append(announcement.getAnnouncementTitle());
            sb.append("\n");

            sb.append("报名时间：");
            sb.append(announcement.getApplyDate());
            sb.append("\n");

            sb.append("名额：");
            sb.append(announcement.getAccount());
            sb.append("\n");

            sb.append("考试时间：");
            sb.append(announcement.getTestingDate());
            sb.append("\n");

            sb.append("考试地点：");
            sb.append(announcement.getTestingAddress());
            sb.append("\n");

            sb.append("通知链接：");
            sb.append(announcement.getAnnouncementUrl());
            sb.append("\n");
        }
        return sb.toString();
    }


    private List<Announcement> allNeedToSendNotification() {
        return em.createQuery("select o from Announcement o where o.hasNotified=:state and o.testingAddress like :addr", Announcement.class)
                .setParameter("state", false)
                .setParameter("addr", "%合肥市%")
                .getResultList();
    }
}
