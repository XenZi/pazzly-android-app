package com.example.pazzly.domain.entity;

public class MojBroj extends Game {

    public MojBroj() {
    }

    public MojBroj(String gameName, int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(gameName, rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound, 1);
    }
}
