package com.example.pazzly.domain.entity;

public class Skocko extends Game{
    public Skocko() {

    }

    public Skocko(int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound,1);
    }
}
