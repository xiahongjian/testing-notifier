package tech.hongjian.testingnotifier.service;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hongjian.testingnotifier.entity.Dict;
import tech.hongjian.testingnotifier.entity.DictValue;
import tech.hongjian.testingnotifier.repository.DictRepository;
import tech.hongjian.testingnotifier.repository.DictValueRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@Service
public class DictService {
    @Setter(onMethod_ = {@Autowired})
    private DictRepository dictRepository;

    @Setter(onMethod_ = {@Autowired})
    private DictValueRepository dictValueRepository;

    public Dict getDictById(Integer id) {
        return Optional.ofNullable(id).flatMap(dictRepository::findById).orElse(null);
    }

    public Dict getDictByKey(String key) {
        return Optional.ofNullable(key).map(dictRepository::findFirstByKey).orElse(null);
    }

    public DictValue getDictValueByKey(String key) {
        return StringUtils.isBlank(key) ? null : dictValueRepository.findFirstByKey(key);
    }

    public List<Dict> listDict() {
        return dictRepository.findAll();
    }

    public List<DictValue> listValueByKey(String key) {
        return Optional.ofNullable(getDictByKey(key))
                .map(d -> listEnableValueById(d.getId()))
                .orElse(Collections.emptyList());
    }

    public List<DictValue> listValueById(Integer id) {
        return dictValueRepository.findAllByDictId(id);
    }

    public List<DictValue> listEnableValueByKey(String key) {
        return Optional.ofNullable(getDictByKey(key))
                .map(d -> listEnableValueById(d.getId()))
                .orElse(Collections.emptyList());
    }

    public Map<String, String> getEnableValueMap(String key) {
        return listEnableValueByKey(key).stream().collect(Collectors.toMap(DictValue::getKey, DictValue::getLabel));
    }

    public List<DictValue> listEnableValueById(Integer id) {
        return dictValueRepository.findEnableValue(id);
    }

    public void deleteById(Integer id) {
        if (id != null) {
            dictRepository.deleteById(id);
            dictValueRepository.deleteAllByDictId(id);
        }
    }

    public void deleteDictValue(Integer dictValueId) {
        if (dictValueId != null) {
            dictValueRepository.deleteById(dictValueId);
        }
    }

    public Dict createDict(Dict dict) {
        if (dictRepository.existsByKey(dict.getKey())) {
            throw new ServiceException("Key???" + dict.getKey() + "???????????????????????????");
        }
        return dictRepository.save(dict);
    }

    public Dict updateDict(Integer id, Dict dict) {
        return Optional.ofNullable(getDictById(id)).map(d -> {
            BeanUtils.copyProperties(dict, d);
            return dictRepository.save(d);
        }).orElse(null);
    }

    public DictValue createDictValue(Integer dictId, DictValue dictValue) {
        if (dictValueRepository.existsByDictIdAndValue(dictId, dictValue.getValue())) {
            throw new ServiceException("Value???" + dictValue.getValue() + "???????????????????????????");
        }
        dictValue.setDictId(dictId);
        return dictValueRepository.save(dictValue);
    }

    public DictValue updateDictValue(Integer dictValueId, DictValue dictValue) {
        return dictValueRepository.findById(dictValueId).map(value -> {
            BeanUtils.copyProperties(dictValue, value);
            return dictValueRepository.save(value);
        }).orElse(null);
    }
}
