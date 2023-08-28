package com.example.pazzly.domain.entity;

public class KorakPoKorak extends Game{

    public KorakPoKorak() {
    }

    public KorakPoKorak(int rounds, int maxPointsPerRound, int minimalPointsPerRound, double durationPerRound) {
        super(rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound, 1);
    }
}
