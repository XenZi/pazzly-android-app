package com.example.pazzly.domain.entity;

public class KorakPoKorak extends Game{

    public KorakPoKorak() {
    }

    public KorakPoKorak(int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound, 1);
    }
}
