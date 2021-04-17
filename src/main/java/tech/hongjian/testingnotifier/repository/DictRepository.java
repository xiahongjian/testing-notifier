package tech.hongjian.testingnotifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.hongjian.testingnotifier.entity.Dict;

import java.util.List;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@Repository
public interface DictRepository extends JpaRepository<Dict, Integer> {
    Dict findFirstByKey(String key);

    boolean existsByKey(String key);
}
