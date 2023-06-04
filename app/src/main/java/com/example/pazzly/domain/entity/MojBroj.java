package com.example.pazzly.domain.entity;

public class MojBroj extends Game {

    public MojBroj() {
    }

    public MojBroj(int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound, 1);
    }
}
