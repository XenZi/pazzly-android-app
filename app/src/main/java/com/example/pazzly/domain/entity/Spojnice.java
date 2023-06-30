package com.example.pazzly.domain.entity;

public class Spojnice extends Game{

    public Spojnice(){

    }
    public Spojnice(int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound,1);
    }


}
