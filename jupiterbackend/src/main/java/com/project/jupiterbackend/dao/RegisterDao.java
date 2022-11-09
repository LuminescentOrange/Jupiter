package com.project.jupiterbackend.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.project.jupiterbackend.entity.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;

@Repository
public class RegisterDao {

    @Autowired
    private SessionFactory sessionFactory; // belong to hibernate

    public boolean register(User user) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(user); // 创建user，存到db
            session.getTransaction().commit();
            // return true;//在这return也行
        } catch (PersistenceException | IllegalStateException ex) {
            // if hibernate throws this exception, it means the user already be register
            ex.printStackTrace(); //可以在consolelog里看到exception，不然因为catch住了看不到
            session.getTransaction().rollback();
            return false;
        } finally {
            if (session != null) session.close();
        }
        return true;
        //return false;
    }
}
