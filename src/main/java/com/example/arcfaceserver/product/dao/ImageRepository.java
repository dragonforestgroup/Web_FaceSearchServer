package com.example.arcfaceserver.product.dao;

import com.example.arcfaceserver.product.beans.Image;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ImageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean save(Image image) {
        try {
            entityManager.persist(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public Image findByUid(String uid) {
        try {
            String sql = "from Image where uid= '" + uid + "'";
            Query query = entityManager.createQuery(sql);
            List<Image> resultList = query.getResultList();
            if(resultList!=null&&resultList.size()>0){
                return resultList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
