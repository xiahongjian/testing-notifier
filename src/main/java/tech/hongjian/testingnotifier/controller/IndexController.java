package tech.hongjian.testingnotifier.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hongjian.testingnotifier.parser.AnnouncementParser;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@RestController
public class IndexController {
    @Setter(onMethod_ = {@Autowired})
    private AnnouncementParser parser;

    @GetMapping("/parse")
    public String parse() {

        parser.parse(1311);
        return "done";
    }
}
