package com.project.jupiterbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.jupiterbackend.entity.response.Game;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/game")
public class HelloServlet extends HttpServlet {

    /* doGet, doPost语意区别,get是从服务器上获取数据，post是向服务器传送数据。
       GET方式：安全性差。因为是直接将数据显示在地址栏中，浏览器有缓冲，可记录用户信息。所以安全性低。不能传 只能拿
       POST方式：安全性高。因为post方式提交数据时是采用的HTTP post机制，是将表单中的字段与值放置在HTTP HEADER内一起传送到ACTION所指的URL中，用户是看不见的。
     */
    // postman 上半部分相当于后端, response部分是后端往前端传的数据, 顾名思义
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // response.getWriter().print("Hello World");
        // ============== 手动 ================
        // String gamename = request.getParameter("gamename");// 前段发送 后端收到request localhost:8080/game?gamename=abc
        // response.getWriter().print("Game is: " + gamename); //后段返回response Game is: abc 前端收到

        // ================  json =============
        // 获取json格式的数据，这里是手动输入, 拿数据
//        response.setContentType("application/json");
//        JSONObject game = new JSONObject();
//        game.put("name", "World of Warcraft"); //put 前段  "name", "World of Warcraft"   发送到后段
//        game.put("developer", "Blizzard Entertainment");
//        game.put("release_time", "Feb 11, 2005");
//        game.put("website", "https://www.worldofwarcraft.com");
//        game.put("price", 49.99);
//
//        // Write game information to response body
//        response.getWriter().print(game); // 发送给前端 展示在postman response modules

        // ================= jackson entity.response.Game============== 不需要手动put & getString, jackson把class变成json格式
        response.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        Game.Builder builder = new Game.Builder();
        builder.setName("World of Warcraft");
        builder.setDeveloper("Blizzard Entertainment");
        builder.setReleaseTime("Feb 11, 2005");
        builder.setWebsite("https://www.worldofwarcraft.com");
        builder.setPrice(49.99);

        Game game = builder.build();
        // game是一个java object
        // Write game information to response body
        // 用mapper把object转换成json!!
        response.getWriter().print(mapper.writeValueAsString(game));


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ============ json ===========
        // Read game information from request body
        JSONObject jsonRequest = new JSONObject(IOUtils.toString(request.getReader()));
        //从req里拿到body, JSONObject是转换成json格式。以string类型传入了json,这个数据是postman给的，不是上面doGet给的
        String name = jsonRequest.getString("name"); // post 后端拿到name 前段数据
        String developer = jsonRequest.getString("developer");
        String releaseTime = jsonRequest.getString("release_time");
        String website = jsonRequest.getString("website");
        float price = jsonRequest.getFloat("price");
        // Print game information to IDE console
        System.out.println("Name is: " + name); // post 后端拿到name, print Name is: World of Warcraft, 只是一个test
        System.out.println("Developer is: " + developer);
        System.out.println("Release time is: " + releaseTime);
        System.out.println("Website is: " + website);
        System.out.println("Price is: " + price);
        // Return status = ok as response body to the client
        response.setContentType("application/json");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "ok"); // 给了前端一个 ok 的response,是json格式
        response.getWriter().print(jsonResponse);
    }


    public void destroy() {
    }
}