package com.project.jupiterbackend.service;

import com.project.jupiterbackend.dao.RegisterDao;
import com.project.jupiterbackend.entity.db.User;
import com.project.jupiterbackend.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegisterService {
// 套娃
    @Autowired
    private RegisterDao registerDao;

    public boolean register(User user) throws IOException {
        user.setPassword(Util.encryptPassword(user.getUserId(), user.getPassword())); // encrypted password in util

        return registerDao.register(user);
    }
}