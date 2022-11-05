package com.project.jupiterbackend.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.jupiterbackend.entity.db.Item;

public class FavoriteRequestBody { //前段发给后端用户喜欢的，后端存到db

    @JsonProperty("favorite")
    private Item favoriteItem; //json里favorite map到 favoriteItem

    public Item getFavoriteItem() {
        return favoriteItem;
    }
}
