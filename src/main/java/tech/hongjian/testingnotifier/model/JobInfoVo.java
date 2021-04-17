package tech.hongjian.testingnotifier.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import tech.hongjian.testingnotifier.entity.JobInfo;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JobInfoVo extends JobInfo {
    private JobState state;

    public JobInfoVo(JobInfo jobInfo) {
        BeanUtils.copyProperties(jobInfo, this);
    }
}
