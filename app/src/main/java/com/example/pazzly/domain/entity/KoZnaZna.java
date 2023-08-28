package com.example.pazzly.domain.entity;

public class KoZnaZna extends Game {

    public KoZnaZna() {}

    public KoZnaZna(int rounds, int maxPointsPerRound, int minimalPointsPerRound, double durationPerRound) {
        super(rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound,1);
    }
}
