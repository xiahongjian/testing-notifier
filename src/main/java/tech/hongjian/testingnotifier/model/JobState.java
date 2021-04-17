package tech.hongjian.testingnotifier.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by xiahongjian on 2021/4/17.
 */
public enum JobState {
    //NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
    NONE(0, "不存在"),
    NORMAL(1, "正常"),
    PAUSED(2, "暂停"),
    COMPLETE(3, "完成"),
    ERROR(4, "错误"),
    BLOCKED(5, "阻塞");

    private int value;
    private String description;

    JobState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }

    public static JobState of(int value) {
        for (JobState jobState : values()) {
            if (jobState.getValue() == value) {
                return jobState;
            }
        }
        return null;
    }
}
