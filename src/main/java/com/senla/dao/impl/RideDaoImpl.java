package com.senla.dao.impl;

import com.senla.dao.RideDao;
import com.senla.model.entity.Ride;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.User;
import com.senla.model.entityenum.RideStatus;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RideDaoImpl extends AbstractDaoImpl<Ride> implements RideDao {

    @Override
    protected Class<Ride> daoEntityClass() {
        return Ride.class;
    }


    /**
     * @param user User, whose rides are looking for, except rides with PENDING status
     * @param limit Last N rides
     * @return {@link List} of {@link Ride}
     */
    @Override
    public List<Ride> getRidesOfTheUser(User user, RideStatus rideStatus, Integer limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ride> cq = cb.createQuery(Ride.class);
        Root<Ride> root = cq.from(Ride.class);

        Predicate predicateForUser = cb.equal(root.get("user"), user);
        Predicate predicateForNotPendingStatus = cb.equal(root.get("status"), rideStatus);
        Order order = new OrderImpl(root.get("id"), false);

        TypedQuery<Ride> q = entityManager.createQuery(cq
                .select(root)
                .where(cb.and(predicateForUser, predicateForNotPendingStatus))
                .orderBy(order)
        );
        q.setMaxResults(limit);

        return q.getResultList();
    }

    @Override
    public List<Ride> getRidesOfTheScooter(Scooter scooter, Integer limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ride> cq = cb.createQuery(Ride.class);
        Root<Ride> root = cq.from(Ride.class);

        Order order = new OrderImpl(root.get("startTimestamp"), false);
        TypedQuery<Ride> q = entityManager.createQuery(cq
                .select(root)
                .where(cb.equal(root.get("scooter"), scooter))
                .orderBy(order)
        );
        q.setMaxResults(limit);

        return q.getResultList();
    }

    @Override
    public List<Ride> getRidesOfTheScooter(Scooter scooter,
                                           LocalDateTime firstRideStartTimestamp,
                                           LocalDateTime lastRideStartTimestamp) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ride> cq = cb.createQuery(Ride.class);
        Root<Ride> root = cq.from(Ride.class);

        Order order = new OrderImpl(root.get("startTimestamp"), true);
        Predicate predicateForScooter = cb.equal(root.get("scooter"), scooter);
        Predicate predicateForStartTimestamp = cb.greaterThanOrEqualTo(root.get("startTimestamp"), firstRideStartTimestamp);
        Predicate predicateForEndTimestamp = cb.lessThanOrEqualTo(root.get("endTimestamp"), lastRideStartTimestamp);

        TypedQuery<Ride> q = entityManager.createQuery(cq
                .select(root)
                .where(cb.and(predicateForScooter, cb.and(predicateForStartTimestamp, predicateForEndTimestamp)))
                .orderBy(order)
        );

        return q.getResultList();
    }

    @Override
    public void deletePendingRides(Duration minTimePending) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Ride> cq = cb.createCriteriaDelete(Ride.class);
        Root<Ride> root = cq.from(Ride.class);

        Predicate predicateForOverdue = cb.lessThan(root.get("creationTimestamp"), LocalDateTime.now().minus(minTimePending));
        Predicate predicateForPending = cb.equal(root.get("status"), RideStatus.PENDING);

        entityManager.createQuery(cq.where(cb.and(predicateForOverdue, predicateForPending))).executeUpdate();
    }
}
