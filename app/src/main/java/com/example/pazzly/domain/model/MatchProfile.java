package com.example.pazzly.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MatchProfile implements Serializable {
    private String UUID;
    private int points;
    private String username;
    private String image;

    public MatchProfile(String UUID, int points, String username, String image) {
        this.UUID = UUID;
        this.points = points;
        this.username = username;
        this.image = image;
    }

    public MatchProfile() {
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static MatchProfile fromJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String id = jsonObject.optString("id");
            int points = jsonObject.optInt("points");
            String username = jsonObject.optString("username");
            String image = jsonObject.optString("image");
            return new MatchProfile(id, points, username, image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


