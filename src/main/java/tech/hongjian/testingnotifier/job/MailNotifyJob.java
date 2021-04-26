package tech.hongjian.testingnotifier.job;

import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import tech.hongjian.testingnotifier.service.NotificationService;
import tech.hongjian.testingnotifier.util.ScheduleUtil;

/**
 * Created by xiahongjian on 2021/4/21.
 */
@Component
public class MailNotifyJob implements Job {
    @Setter(onMethod_ = {@Autowired})
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:hongjian.xia@qq.com}")
    private String from;

    @Setter(onMethod_ = {@Autowired})
    private NotificationService notificationService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String to = ScheduleUtil.getJobStringParam(context, "to");
        String htmlContent = ScheduleUtil.getJobStringParam(context, "content");
        if (StringUtils.isBlank(htmlContent)) {
            htmlContent = "<h1>标题</h1>\n" +
                    "\n" +
                    "<table>\n" +
                    "    <tr>\n" +
                    "        <th>第一列</th>\n" +
                    "        <th>第二列</th>\n" +
                    "        <th>第二列</th>\n" +
                    "    " +
                    "</tr>\n" +
                    "    <tr>\n" +
                    "        <td>1</td>\n" +
                    "        <td><a href='https://baidu.com' target='_blank'>https://baidu.com</a></td>\n" +
                    "        <td>百度</td>\n" +
                    "    " +
                    "</tr>\n" +
                    "    <tr>\n" +
                    "        <td>2</td>\n" +
                    "        <td><a href='https://baidu.com' target='_blank'>https://baidu.com</a></td>\n" +
                    "        <td>百度</td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>3</td>\n" +
                    "        <td><a href='https://baidu.com' target='_blank'>https://baidu.com</a></td>\n" +
                    "        <td>百度</td>\n" +
                    "    </tr>\n" +
                    "</table>";
        }
        String subject = ScheduleUtil.getJobStringParam(context, "subject");
        if (StringUtils.isBlank(subject)) {
            subject = "测试邮件";
        }
        notificationService.sendMail(new String[]{to}, subject, htmlContent);
    }
}
