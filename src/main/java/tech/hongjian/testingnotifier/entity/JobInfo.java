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
    private String className;
    private String cron;
    private String remark;
    private String params;
    private Boolean enable;
}
