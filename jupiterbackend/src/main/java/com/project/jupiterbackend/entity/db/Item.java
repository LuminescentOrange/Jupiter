package com.project.jupiterbackend.entity.db;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity //map class to table
@Table(name = "items") // map database table name
@JsonIgnoreProperties(ignoreUnknown = true) //可以ignore不认识的
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item implements Serializable { //传给别人 打个包  存硬盘 etc

    //https://www.baeldung.com/java-serial-version-uid
    private static final long serialVersionUID = 1L;
    //做了改变之后，hibernate识别version number有没有变，需不需要更新
    @Id // primary key
    @JsonProperty("id")
    private String id; //没有@Column是因为名字和database里一样

    @JsonProperty("title")
    private String title;
    @JsonProperty("url")
    private String url;
    @Column(name = "thumbnail_url")
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "broadcaster_name")
    @JsonProperty("broadcaster_name")
    @JsonAlias({ "user_name" })
    private String broadcasterName;


    @Column(name = "game_id")
    @JsonProperty("game_id")
    private String gameId;
    @Enumerated(value = EnumType.STRING)
    @JsonProperty("item_type")
    private ItemType type;

    @JsonIgnore
    @ManyToMany(mappedBy = "itemSet") //user
    private Set<User> users= new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getBroadcasterName() {
        return broadcasterName;
    }

    public void setBroadcasterName(String broadcasterName) {
        this.broadcasterName = broadcasterName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}