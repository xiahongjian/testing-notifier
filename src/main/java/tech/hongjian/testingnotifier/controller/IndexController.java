package tech.hongjian.testingnotifier.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.testingnotifier.model.JobInfoVo;
import tech.hongjian.testingnotifier.service.DictService;
import tech.hongjian.testingnotifier.service.JobService;
import tech.hongjian.testingnotifier.service.SysDictKeys;

import java.util.List;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@Controller
public class IndexController {

    @Setter(onMethod_ = {@Autowired})
    private JobService jobService;

    @Setter(onMethod_ = {@Autowired})
    private DictService dictService;

    @Setter(onMethod_ = {@Autowired})
    private ObjectMapper objectMapper;

    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model) {
        return jobState(model);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/schedules")
    public String jobState(Model model) {
        model.addAttribute("triggerMap", toJson(dictService.getEnableValueMap(SysDictKeys.TRIGGER_TYPE)));
//        model.addAttribute("schedules", jobService.listScheduledJob());
        return "index";
    }

    @GetMapping("/jobs")
    public String jobs(Model model) {
        model.addAttribute("triggerMap", toJson(dictService.getEnableValueMap(SysDictKeys.TRIGGER_TYPE)));
//        model.addAttribute("jobs", jobService.listJobInfo());
        return "job";
    }
}
