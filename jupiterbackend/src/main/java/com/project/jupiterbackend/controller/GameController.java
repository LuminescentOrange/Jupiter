package com.project.jupiterbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.jupiterbackend.service.GameService;
import com.project.jupiterbackend.service.TwitchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// controller整体：收到http请求之后，获取并处理，然后不用的controller调用对应的业务逻辑，call service
@Controller
public class GameController {


    // private GameService gameService = new GameService(); // 不用injection test
    @Autowired
    private GameService gameService; //@Service里面包括了@Component，@Controller也是

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public void getGame(@RequestParam(value = "game_name", required = false) String gameName
            , HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            // Return the dedicated game information if gameName is provided in the request URL, otherwise return the top x games.
            if (gameName != null) {
                response.getWriter().print(new ObjectMapper().writeValueAsString(gameService.searchGame(gameName)));
            } else {
                response.getWriter().print(new ObjectMapper().writeValueAsString(gameService.topGames(0)));
            }
        } catch (TwitchException e) {
            throw new ServletException(e);
        }
    }

    // /game?game_name=whatever
    // fetch data from url /game?game_name=whatever，game_name 这里fetch到的是whatever
//    @RequestMapping(value = "/game", method = RequestMethod.GET) // map相对路径 /game，+ 方法
//    public void getGame(@RequestParam(value = "game_name", required = false) String gameName
//            , HttpServletResponse response) throws IOException {
    // required: param是不是必须得有
//        String fakeGame = gameService.fakeGame(); // injection test
//        response.getWriter().write(fakeGame); // injection test
//    }

//    @RequestMapping(value = "/search", method = RequestMethod.GET) // map相对路径 /game，+ 方法
//    @ResponseBody  // 发送response，用responsebody
//    public String search(@RequestParam(value = "lon") double lon, @RequestParam(value = "lat") double lat){
//        return "hello search lat: " + lat + ", lon: " + lon;
//    }
//
//    @RequestMapping(value = "/searchrequired", method = RequestMethod.GET) // map相对路径 /game，+ 方法
//    @ResponseBody  // 发送response，用responsebody
//    public String search(@RequestParam(value = "lon", required = false) Double lon, @RequestParam(value = "lat", required = false) Double lat){
//        return "hello search lat: " + lat + ", lon: " + lon;
//    }
}
