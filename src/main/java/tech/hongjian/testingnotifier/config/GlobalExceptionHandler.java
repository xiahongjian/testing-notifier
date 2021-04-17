package tech.hongjian.testingnotifier.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.hongjian.testingnotifier.service.ServiceException;
import tech.hongjian.testingnotifier.util.R;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R globalException(HttpServletResponse response, Exception e) {
        log.info(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public R serviceException(HttpServletResponse response, ServiceException e) {
        log.info(e.getMessage(), e);
        return R.error(e.getMessage());
    }
}
