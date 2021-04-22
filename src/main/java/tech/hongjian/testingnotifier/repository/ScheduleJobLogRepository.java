package tech.hongjian.testingnotifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.hongjian.testingnotifier.entity.ScheduleJobLog;

/**
 * Created by xiahongjian on 2021/4/22.
 */
public interface ScheduleJobLogRepository extends JpaRepository<ScheduleJobLog, Integer> {
}
