package tech.hongjian.testingnotifier.parser;

import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.hongjian.testingnotifier.entity.Announcement;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@Transactional
@Component
public class AnnouncementParser extends BaseParser{
    @Setter(onMethod_ = {@Autowired})
    private EntityManager em;

    private static final Pattern TESTING_DATE_PATTERN = Pattern.compile("本次测试时间：(.*?)，");
    private static final Pattern TESTING_ADDRESS = Pattern.compile("测试站地址：(.*)\\s*咨询电话");
    private static final Pattern APPLY_DATE = Pattern.compile("报名时间：(\\d{4}年\\d{1,2}月\\d{1,2}日[\\d:]+起)，");
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("共(\\d+)个名额");

    @Override
    public void parse(Integer startId) {

        try {
            Element element = queryElement(getIndexUrl(), "#catelist_1 ul");
            Elements eles = element.select("li");
            for (Element e : eles) {
                Element aElement = e.select("a").first();
                String link = aElement.attr("href");
                Integer announcementId = Integer.valueOf(link.substring(link.indexOf("=") + 1));
                String title = aElement.text();
                if (startId >= announcementId && !needToParse(title)) {
                    break;
                }
                parseAnnouncement(link, announcementId, title);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseAnnouncement(String href, Integer id, String title) throws IOException {
        try {
            // 停3秒，控制访问频率
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Announcement announcement = new Announcement();

        String url = composeUrl(href);
        Element contentEle = queryElement(url, "#read_content");
        String announcementContent = contentEle.text();

        announcement.setAnnouncementId(id);
        announcement.setAnnouncementUrl(url);
        announcement.setAnnouncementTitle(title);
//        announcement.setAnnouncementContent(announcementContent);

        Matcher matcher = TESTING_DATE_PATTERN.matcher(announcementContent);
        if (matcher.find()) {
            announcement.setTestingDate(matcher.group(1));
        }
        matcher = TESTING_ADDRESS.matcher(announcementContent);
        if (matcher.find()) {
            announcement.setTestngAddress(matcher.group(1));
        }
        matcher = APPLY_DATE.matcher(announcementContent);
        if (matcher.find()) {
            announcement.setApplyDate(matcher.group(1));
        }
        matcher = ACCOUNT_PATTERN.matcher(announcementContent);
        if (matcher.find()) {
            announcement.setAccount(Integer.valueOf(matcher.group(1)));
        }
        em.persist(announcement);
    }

    private boolean needToParse(String title) {
        return title.matches(".*普通话水平测试.*");
    }

    private String composeUrl(String href) {
        String url = getMainDomain();
        if (href.startsWith("/")) {
            href = href.substring(1);
        }
        return url + href;
    }

    @Override
    public String getIndexUrl() {
        return "http://ahywpc.ahedu.gov.cn/artcate.asp?id=15";
    }

    @Override
    public String getMainDomain() {
        return "http://ahywpc.ahedu.gov.cn/";
    }
}
