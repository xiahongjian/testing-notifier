package tech.hongjian.testingnotifier.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Announcement extends BaseEntity {
    private String testingDate;

    private String testingAddress;

    private String applyDate;

    private Integer account;

    private Integer announcementId;

    private String announcementUrl;

    private String announcementTitle;

    private Boolean hasNotified = Boolean.FALSE;
}
