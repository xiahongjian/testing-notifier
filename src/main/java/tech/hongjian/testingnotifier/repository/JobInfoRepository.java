package tech.hongjian.testingnotifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.hongjian.testingnotifier.entity.JobInfo;

import java.util.List;

/**
 * Created by xiahongjian on 2021/4/15.
 */
@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, Integer> {
    List<JobInfo> findAllByEnableOrderById(boolean enable);

    long countByName(String name);
}
