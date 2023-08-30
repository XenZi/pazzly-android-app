package com.example.pazzly.domain.entity;

public class Skocko extends Game{
    public Skocko() {

    }

    public Skocko(String gameName, int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(gameName, rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound,1);
    }
}
