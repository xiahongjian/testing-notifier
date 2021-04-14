package tech.hongjian.testingnotifier.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@Entity
@Data
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    private String testingDate;

    private String testngAddress;

    private String applyDate;

    private Integer account;

    private Integer announcementId;

    private String announcementUrl;

    private String announcementTitle;

//    @Column(length = 4000)
//    private String announcementContent;

    private Boolean hasNotified = Boolean.FALSE;
}
