package com.example.arcfaceserver.dao;

import com.example.arcfaceserver.enity.FaceEntity;
import org.hibernate.query.criteria.internal.predicate.ExplicitTruthValueCheck;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class FaceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean save(FaceEntity entity) {
        try {
            entityManager.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public List<FaceEntity> findByGroupId(int groupId) {
        try {
            String sql = "";
            if (groupId == -1) {
                sql = "from FaceEntity";
            } else {
                sql = "from FaceEntity where groupId=" + groupId;
            }
            Query query = entityManager.createQuery(sql);
            List<FaceEntity> resultList = query.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public List<Integer> getGroupList() {
        try {
            String sql = "select distinct groupId from FaceEntity";
            Query query = entityManager.createQuery(sql);
            List<Integer> groupIdList = query.getResultList();
            return groupIdList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
