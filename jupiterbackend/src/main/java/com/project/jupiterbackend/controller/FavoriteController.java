package com.project.jupiterbackend.controller;

import com.project.jupiterbackend.entity.db.Item;
import com.project.jupiterbackend.entity.request.FavoriteRequestBody;
import com.project.jupiterbackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping(value = "/favorite", method = RequestMethod.POST)
    public void setFavoriteItem(@RequestBody FavoriteRequestBody requestBody, HttpServletRequest request, HttpServletResponse response) {

        // check登陆 log in
        HttpSession session = request.getSession(false); // 没有session就不创建，null是没登陆
        // 拿到登陆用户信息
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ;
        }
        // 登陆信息拿到userid
        String userId = (String) session.getAttribute("user_id");
//        String userId = "5678"; //hard code userid
        favoriteService.setFavoriteItem(userId, requestBody.getFavoriteItem());
    }

    @RequestMapping(value = "/favorite", method = RequestMethod.DELETE)
    public void unsetFavoriteItem(@RequestBody FavoriteRequestBody requestBody, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String userId = (String) session.getAttribute("user_id");
//        String userId = "5678"; //hard code userid
        favoriteService.unsetFavoriteItem(userId, requestBody.getFavoriteItem().getId());
    }

    @RequestMapping(value = "/favorite", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<Item>> getFavoriteItem(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return new HashMap<>();
        }
        String userId = (String) session.getAttribute("user_id");
//
//        String userId = "5678"; //hard code userid
        return favoriteService.getFavoriteItems(userId);
    }
}


