package tech.hongjian.testingnotifier.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hongjian.testingnotifier.entity.Announcement;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by xiahongjian on 2021/4/14.
 */
@Transactional
@Service
public class NotificationService {
    @Setter(onMethod_ = {@Autowired})
    private EntityManager em;

    public void sendNotification() {

    }

    private List<Announcement> allNeedToSendNotification() {
        return em.createQuery("select o from Announcement o where o.hasNotified=:state and o.testngAddress like :addr", Announcement.class)
                .setParameter("state", false)
                .setParameter("addr", "%合肥市%")
                .getResultList();
    }
}
