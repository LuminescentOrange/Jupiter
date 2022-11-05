package com.project.jupiterbackend.dao;

import com.project.jupiterbackend.entity.db.Item;
import com.project.jupiterbackend.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FavoriteDao { // database access object 面向对象的数据库接口
    @Autowired
    private SessionFactory sessionFactory; // 在applicationconfig里创建

    //我的理解是这些操作都在joint table也就是favourite_id上操作，删掉的时候删掉的只有favourite_id里的


    // Insert a favorite record to the database
    public void setFavoriteItem(String userId, Item item) {
        Session session = null;// session: 从打开到关闭db的一次操作

        try {
            session = sessionFactory.openSession();//打开session
            // SELECT * FROM users WHERE user_id = '123'
            User user = session.get(User.class, userId); // get userId
            user.getItemSet().add(item); //把item加进user，数据库层面user这个obj整体操作(!!)
            session.beginTransaction();  // 开始操作
            // insert xxx into user
            session.save(user); // 把变化的user写进去，save是insert
            session.getTransaction().commit(); // 写进去 commit
        } catch (Exception ex) { // 出错了rollback
            ex.printStackTrace();
            session.getTransaction().rollback(); // rollback
        } finally {
            if (session != null) session.close(); //不管咋样都要把session close
        }
    }

    // Remove a favorite record from the database
    public void unsetFavoriteItem(String userId, String itemId) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            Item item = session.get(Item.class, itemId);
            user.getItemSet().remove(item);
            session.beginTransaction();
            session.update(user); // delete是make change，insert是有新建obj所以用save，insert用update会有exception
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    public Set<Item> getFavoriteItems(String userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, userId).getItemSet();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new HashSet<>();
    }
}
