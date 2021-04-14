package tech.hongjian.testingnotifier.parser;
/**
 * @author xiahongjian
 * @time   2019-12-21 16:24:38
 */

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public abstract class BaseParser implements Parser {

    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).userAgent(USER_AGENT).get();
    }

    public Element queryElement(String url, String selector) throws IOException {
        return Jsoup.connect(url).userAgent(USER_AGENT).get().select(selector).first();
    }

    public Elements queryElements(String url, String selector) throws IOException {
        return Jsoup.connect(url).userAgent(USER_AGENT).get().select(selector);
    }

    public String processUrl(String path) {
        if (StringUtils.isBlank(path)) {
            return path;
        }
        String url = path;
        if (url.startsWith("//")) {
            return "https:" + url;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        if (!url.contains(getMainDomain())) {
            return getIndexUrl() + (url.startsWith("/") ? "" : "/") + path;
        }
        return url;
    }
}
