package com.example.pazzly.domain.entity;

public class Asocijacija extends Game{

    public Asocijacija() {
    }

    public Asocijacija(int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound, 1);
    }
}
