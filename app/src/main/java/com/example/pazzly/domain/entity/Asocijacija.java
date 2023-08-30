package com.example.pazzly.domain.entity;

public class Asocijacija extends Game{

    public Asocijacija() {
    }

    public Asocijacija(String gameName, int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(gameName, rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound, 1);
    }
}
