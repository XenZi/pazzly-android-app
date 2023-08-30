package com.example.pazzly.domain.entity;

public class KoZnaZna extends Game {

    public KoZnaZna() {}

    public KoZnaZna(String gameName, int rounds, int maxPointsPerRound, int minimalPointsPerRound, double durationPerRound) {
        super(gameName, rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound,1);
    }
}
