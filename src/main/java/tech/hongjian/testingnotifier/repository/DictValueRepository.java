package tech.hongjian.testingnotifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.hongjian.testingnotifier.entity.DictValue;

import java.util.List;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@Repository
public interface DictValueRepository extends JpaRepository<DictValue, Integer> {
    List<DictValue> findAllByDictId(Integer dictId);

    @Query("select o from DictValue o where o.dictId=:dictId and o.enable=true")
    List<DictValue> findEnableValue(Integer dictId);

    void deleteAllByDictId(Integer dictId);

    boolean existsByDictIdAndValue(Integer dictId, String value);

    DictValue findFirstByKey(String key);
}
