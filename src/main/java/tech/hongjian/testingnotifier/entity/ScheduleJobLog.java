package tech.hongjian.testingnotifier.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by xiahongjian on 2021/4/22.
 */
@Data
@Entity
public class ScheduleJobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime startAt;
    private LocalDateTime finishAt;
    private Long duration;
    private String jobClass;
    private String params;
    private Boolean success;
}
