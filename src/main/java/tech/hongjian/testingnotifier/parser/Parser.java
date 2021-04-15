package tech.hongjian.testingnotifier.parser;

/**
 * Created by xiahongjian on 2021/4/14.
 */
public interface Parser {
    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";

    void parse(Integer startId, int delay);

    String getIndexUrl();

    String getMainDomain();
}
