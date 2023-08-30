package com.example.pazzly.domain.entity;

public abstract class Game {
    int rounds;
    int maxPointsPerRound;
    int minimalPointsPerRound;
    double durationPerRound;
    int currentRound;
    String gameName;
    public Game() {
    }

    public Game(String gameName, int rounds, int maxPointsPerRound, int minimalPointsPerRound, double durationPerRound, int currentRound) {
        this.gameName = gameName;
        this.rounds = rounds;
        this.maxPointsPerRound = maxPointsPerRound;
        this.minimalPointsPerRound = minimalPointsPerRound;
        this.durationPerRound = durationPerRound;
        this.currentRound = currentRound;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getMaxPointsPerRound() {
        return maxPointsPerRound;
    }

    public void setMaxPointsPerRound(int maxPointsPerRound) {
        this.maxPointsPerRound = maxPointsPerRound;
    }

    public int getMinimalPointsPerRound() {
        return minimalPointsPerRound;
    }

    public void setMinimalPointsPerRound(int minimalPointsPerRound) {
        this.minimalPointsPerRound = minimalPointsPerRound;
    }

    public double getDurationPerRound() {
        return durationPerRound;
    }

    public void setDurationPerRound(double durationPerRound) {
        this.durationPerRound = durationPerRound;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
