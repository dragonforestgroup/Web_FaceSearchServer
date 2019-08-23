package com.example.arcfaceserver.product.dao;

import com.example.arcfaceserver.product.beans.Image;
import com.example.arcfaceserver.product.beans.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public boolean register(Person person, Image image){
        try {
            entityManager.persist(person);
            entityManager.persist(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
