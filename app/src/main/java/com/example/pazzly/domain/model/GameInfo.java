package com.example.pazzly.domain.model;

public class GameInfo {
    private String playerUUID;
    private int pointsAchieved;

    public GameInfo() {
    }

    public GameInfo(String playerUUID, int pointsAchieved) {
        this.playerUUID = playerUUID;
        this.pointsAchieved = pointsAchieved;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public int getPointsAchieved() {
        return pointsAchieved;
    }

    public void setPointsAchieved(int pointsAchieved) {
        this.pointsAchieved = pointsAchieved;
    }
}
