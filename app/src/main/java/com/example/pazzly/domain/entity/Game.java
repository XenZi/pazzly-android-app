package com.example.pazzly.domain.entity;

public abstract class Game {
    int rounds;
    int maxPointsPerRound;
    int minimalPointsPerRound;
    int durationPerRound;
    int currentRound;

    public Game() {
    }

    public Game(int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound, int currentRound) {
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

    public int getDurationPerRound() {
        return durationPerRound;
    }

    public void setDurationPerRound(int durationPerRound) {
        this.durationPerRound = durationPerRound;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
