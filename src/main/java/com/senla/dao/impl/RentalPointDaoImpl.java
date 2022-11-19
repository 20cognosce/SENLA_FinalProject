package com.senla.dao.impl;

import com.senla.dao.RentalPointDao;
import com.senla.domain.model.entity.RentalPoint;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class RentalPointDaoImpl extends AbstractDaoImpl<RentalPoint> implements RentalPointDao {

    @Override
    protected Class<RentalPoint> daoEntityClass() {
        return RentalPoint.class;
    }

    @Override
    public List<RentalPoint> getAllTheClosest(double latitude, double longitude, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalPoint> cq = cb.createQuery(RentalPoint.class);
        Root<RentalPoint> root = cq.from(RentalPoint.class);

        ParameterExpression<Double> doubleParam1 = cb.parameter(Double.class);
        ParameterExpression<Double> doubleParam2 = cb.parameter(Double.class);
        Expression<Double> func =
                cb.function("calculate_distance_to_point", Double.class, root.get("id"), doubleParam1, doubleParam2);

        TypedQuery<RentalPoint> q = entityManager.createQuery(cq
                .select(root)
                .orderBy(new OrderImpl(func, true))
        );
        q.setParameter(doubleParam1, latitude);
        q.setParameter(doubleParam2, longitude);
        q.setMaxResults(limit);

        return q.getResultList();
    }

    @Override
    public Optional<RentalPoint> getByIdWithScooters(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalPoint> cq = cb.createQuery(RentalPoint.class);
        Root<RentalPoint> root = cq.from(RentalPoint.class);

        root.fetch("scooters", JoinType.LEFT);

        TypedQuery<RentalPoint> q = entityManager.createQuery(cq
                .select(root)
                .where(cb.equal(root.get("id"), id)));

        RentalPoint rentalPoint = q.getSingleResult();
        return Optional.of(rentalPoint);
    }
}
