package tech.hongjian.testingnotifier.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by xiahongjian on 2021/4/17.
 */
@Data
@Entity
public class DictValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String key;
    private String value;
    private String label;
    private Boolean enable;
    private Integer dictId;
}
