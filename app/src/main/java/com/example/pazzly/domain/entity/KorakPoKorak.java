package com.example.pazzly.domain.entity;

public class KorakPoKorak extends Game{

    public KorakPoKorak() {
    }

    public KorakPoKorak(String gameName, int rounds, int maxPointsPerRound, int minimalPointsPerRound, double durationPerRound) {
        super(gameName, rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound, 1);
    }
}
