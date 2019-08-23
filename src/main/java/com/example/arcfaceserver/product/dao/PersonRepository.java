package com.example.arcfaceserver.product.dao;

import com.example.arcfaceserver.product.beans.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean save(Person person) {
        try {
            entityManager.persist(person);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public List<Person> findByGroupId(int groupId) {
        try {
            String sql = "";
            if (groupId == -1) {
                sql = "from Person";
            } else {
                sql = "from Person where groupId=" + groupId;
            }
            Query query = entityManager.createQuery(sql);
            List<Person> resultList = query.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public List<Person> findBy(int groupId, String vender, String version, String appid) {
        try {
            String sql = "";
            if (groupId == -1) {
                sql = "from Person where vender='"+vender+"' and version='"+version+"' and appid='"+appid+"'";
            } else {
                sql = "from Person where groupid="+groupId+" and vender='"+vender+"' and version='"+version+"' and appid='"+appid+"'";
            }
            Query query = entityManager.createQuery(sql);
            List<Person> resultList = query.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public List<Integer> getGroupList() {
        try {
            String sql = "select distinct groupid from Person";
            Query query = entityManager.createQuery(sql);
            List<Integer> groupIdList = query.getResultList();
            return groupIdList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
