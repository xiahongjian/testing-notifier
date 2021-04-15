package tech.hongjian.testingnotifier.service;

import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tech.hongjian.testingnotifier.entity.BaseEntity;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by xiahongjian on 2021/4/15.
 */
public abstract class BaseService<T extends BaseEntity> {
    @Setter(onMethod_ = {@Autowired})
    private EntityManager em;

    protected Class<T> entityClass;

    public BaseService() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            entityClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
    }

    public T findById(Integer id) {
        if (id == null) {
            return null;
        }
        return em.find(getEntityClass(), id);
    }

    public T save(T entity) {
        em.persist(entity);
        return entity;
    }

    public T update(Integer id, T updateData) {
        T entity = findById(id);
        if (entity != null) {
            BeanUtils.copyProperties(updateData, entity);
            em.merge(entity);
        }
        return entity;
    }

    public List<T> list() {
        return em.createQuery("select o from " + getEntityClass().getSimpleName() + " o order by id").getResultList();
    }

    public void remove(Integer id) {
        T byId = findById(id);
        if (byId != null) {
            em.remove(byId);
        }
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
