package tech.hongjian.testingnotifier.util;

import lombok.Getter;

/**
 * Created by xiahongjian on 2021/4/15.
 */
@Getter
public enum Code {
    OK(20000), // OK
    SERVER_ERROR(50000) // 系统错误
    ;

    private int value;

    Code(int value) {
        this.value = value;
    }
}
