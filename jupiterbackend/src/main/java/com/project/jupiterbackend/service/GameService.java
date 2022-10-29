package com.project.jupiterbackend.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.jupiterbackend.entity.response.Game;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Service
public class GameService {

    //没有injection test
    //public String fakeGame(){ return "this is an example";}

    private static final String TOKEN = "Bearer rt6o1i6h50vstec5maqnilyf7rlrb6";
    private static final String CLIENT_ID = "lan980av7jse3lo991o77xsj66u48d";
    private static final String TOP_GAME_URL = "https://api.twitch.tv/helix/games/top?first=%s";
    private static final String GAME_SEARCH_URL_TEMPLATE = "https://api.twitch.tv/helix/games?name=%s";
    private static final int DEFAULT_GAME_LIMIT = 20;


    // Build the request URL which will be used when calling Twitch APIs, e.g. https://api.twitch.tv/helix/games/top when trying to get top games.
    private String buildGameURL(String url, String gameName, int limit) {
        if (gameName.equals("")) { //没给名，走TOP_GAME_URL，https://api.twitch.tv/helix/games/top?first=%s，limit是%s
            return String.format(url, limit);
        } else {
            try {
                // 如果给了名字，GAME_SEARCH_URL_TEMPLATE，https://api.twitch.tv/helix/games?name=%s，%S是gameName
                // encode一下， Encode special characters in URL, e.g. Star War -> Star%20War
                gameName = URLEncoder.encode(gameName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return String.format(url, gameName);
        }
    }

    // Send HTTP request to Twitch Backend based on the given URL, and returns the body of the HTTP response returned from Twitch backend.
    private String searchTwitch(String url) throws TwitchException { //return json content string,在callback里实现转换
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Callback: Define the response handler to parse and return HTTP response body returned from Twitch
        ResponseHandler<String> responseHandler = response -> {
            int responseCode = response.getStatusLine().getStatusCode();
            // status != 200
            if (responseCode != 200) {
                System.out.println("Response status: " + response.getStatusLine().getReasonPhrase());
                throw new TwitchException("Failed to get result from Twitch API");
            }
            // entity == null
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new TwitchException("Failed to get result from Twitch API");
            }
            //handle
            JSONObject obj = new JSONObject(EntityUtils.toString(entity));
            return obj.getJSONArray("data").toString(); //data拿出来，json "data":[]里面的,拿出来的是string
        };

        try {
            // Define the HTTP request, TOKEN and CLIENT_ID are used for user authentication on Twitch backend
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", TOKEN); //provide
            request.setHeader("Client-Id", CLIENT_ID);
            return httpclient.execute(request, responseHandler);
        } catch (IOException e) { //网络不通
            e.printStackTrace();
            throw new TwitchException("Failed to get result from Twitch API");
        } finally { //不管如何 要关掉
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Convert JSON format data returned from Twitch to an Arraylist of Game objects
    private List<Game> getGameList(String data){
        ObjectMapper mapper = new ObjectMapper();
        try {
            Game[] gameArray = mapper.readValue(data, Game[].class);//Game[].class创建的class是game[] array
            return Arrays.asList(gameArray); //asList转换成list
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse game data from Twitch API");
        }
    }

    // Integrate search() and getGameList() together, returns the top x popular games from Twitch.
    public List<Game> topGames(int limit){
        if (limit <= 0) {
            limit = DEFAULT_GAME_LIMIT;
        }

        String gameURL = buildGameURL(TOP_GAME_URL, "", limit); //get url
        String jsonString = searchTwitch(gameURL); // search
        return getGameList(jsonString); // convert to list
    }

    // Integrate search() and getGameList() together, returns the dedicated game based on the game name.
    public Game searchGame(String gameName){
        List<Game> gameList = getGameList(searchTwitch(buildGameURL(GAME_SEARCH_URL_TEMPLATE, gameName, 0)));
        if (gameList.size() != 0) {
            return gameList.get(0);
        }
        return null;
    }

}






