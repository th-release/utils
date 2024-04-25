package com.peertopeer.utils.repository;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

public class Repository<T> {
    private EntityManagerFactory emf;
    private EntityManager entityManager;

    public Repository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("entities");
        entityManager = emf.createEntityManager();
    }

    private void transaction(Runnable codeBlock) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            codeBlock.run();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            tx.begin();
            codeBlock.run();
            tx.commit();
        }
    }

    public void save(T data) {
        EntityTransaction tx = entityManager.getTransaction();
        Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(data);

        try {
            tx.begin();

            if (id != null) {
                Object existingData = entityManager.find(data.getClass(), id);
                if (existingData != null) {
                    entityManager.merge(data);
                } else {
                    entityManager.persist(data);
                }
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }

            tx.begin();

            if (id != null) {
                Object existingData = entityManager.find(data.getClass(), id);
                if (existingData != null) {
                    entityManager.merge(data);
                } else {
                    entityManager.persist(data);
                }
            }

            tx.commit();
        }
    }

    public void delete(T entity) {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        if (!entityManager.contains(entity)) {
            entity = entityManager.merge(entity);
        }
        entityManager.remove(entity);

        tx.commit();
    }

    protected List<T> query(String jpql, Class<T> resultClass, QueryParameter... parameters) {
        TypedQuery<T> query = entityManager.createQuery(jpql, resultClass);

        if (parameters != null) {
            for (QueryParameter data: parameters) {
                query.setParameter(data.getName(), data.getValue());
            }
        }

        List<T> resultList;
        try {
            resultList = query.getResultList();
        } catch (NoResultException | IndexOutOfBoundsException e) {
            resultList = Collections.emptyList();
        }

        return resultList;
    }
}
