package tech.hongjian.testingnotifier.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hongjian.testingnotifier.entity.Dict;
import tech.hongjian.testingnotifier.entity.DictValue;
import tech.hongjian.testingnotifier.service.DictService;
import tech.hongjian.testingnotifier.util.R;

import java.util.List;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@RestController
@RequestMapping("/dicts")
public class DictController {
    @Setter(onMethod_ = {@Autowired})
    private DictService dictService;

    @GetMapping("")
    public R listDict() {
        return R.ok(dictService.listDict());
    }

    @PostMapping("")
    public R createDict(@RequestBody Dict dict) {
        return R.ok(dictService.createDict(dict));
    }

    @DeleteMapping("/{id}")
    public R deleteDict(@PathVariable Integer id) {
        dictService.deleteById(id);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R updateDict(@PathVariable Integer id, @RequestBody Dict dict) {
        return R.ok(dictService.updateDict(id, dict));
    }

    @PostMapping("/{id}/values")
    public R createValue(@PathVariable Integer id, @RequestBody DictValue dictValue) {
        return R.ok(dictService.createDictValue(id, dictValue));
    }

    @GetMapping("/{id}/values")
    public R listValueById(@PathVariable Integer id) {
        return R.ok(dictService.listValueById(id));
    }

    @GetMapping("/{key}/enableValues")
    public R listEnableValue(@PathVariable String key) {
        return R.ok(dictService.listEnableValueByKey(key));
    }

    @PutMapping("/{dictId}/values/{valueId}")
    public R updateValue(@PathVariable Integer dictId, @PathVariable Integer valueId, @RequestBody DictValue dictValue) {
        return R.ok(dictService.updateDictValue(valueId, dictValue));
    }

    @DeleteMapping("/{dictId}/values/{valueId}")
    public R deleteValue(@PathVariable Integer dictId, @PathVariable Integer valueId) {
        dictService.deleteDictValue(valueId);
        return R.ok();
    }
}
