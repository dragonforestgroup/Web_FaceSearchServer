package com.example.arcfaceserver.product.dao;

import com.example.arcfaceserver.product.beans.Image;
import com.example.arcfaceserver.product.beans.Person;
import com.example.arcfaceserver.util.LogUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class FacedbFactory {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 注册
     *
     * @param person
     * @param image
     * @return
     */
    @Transactional
    public boolean register(Person person, Image image) {
        try {
            entityManager.persist(person);
            entityManager.persist(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public boolean deleteByUname(String uname) {
        try {
            String sql = "from Person where uname='" + uname + "'";
            Query query = entityManager.createQuery(sql);
            List<Person> personList = query.getResultList();
            if (personList.size() > 0) {
                Person person = personList.get(0);
                String sql2 = "from Image where uid='" + person.getUid() + "'";
                query = entityManager.createQuery(sql2);
                List<Image> imageList = query.getResultList();
                if (imageList.size() > 0) {
                    Image image = imageList.get(0);
                    entityManager.remove(person);
                    entityManager.remove(image);
                    return true;
                } else {
                    LogUtil.E("图片未找到");
                }
            } else {
                LogUtil.E("用户名未找到");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public boolean deleteByUid(String uid) {
        try {
            String sql = "from Person where uid='" + uid + "'";
            Query query = entityManager.createQuery(sql);
            List<Person> personList = query.getResultList();
            if (personList.size() > 0) {
                Person person = personList.get(0);
                String sql2 = "from Image where uid='" + uid + "'";
                query = entityManager.createQuery(sql2);
                List<Image> imageList = query.getResultList();
                if (imageList.size() > 0) {
                    Image image = imageList.get(0);
                    entityManager.remove(person);
                    entityManager.remove(image);
                    return true;
                } else {
                    LogUtil.E("图片未找到");
                }
            } else {
                LogUtil.E("用户名未找到");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
