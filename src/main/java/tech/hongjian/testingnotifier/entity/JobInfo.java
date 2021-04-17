package tech.hongjian.testingnotifier.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * Created by xiahongjian on 2021/4/15.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class JobInfo extends BaseEntity {
    private String name;
    private String groupName;
    private String className;
    /**
     * 自定义cron表达式
     */
    private String cron;
    /**
     * 使用预设Trigger
     */
    private String triggerType;
    private String remark;
    private String params;
    private Boolean enable = Boolean.FALSE;
}
