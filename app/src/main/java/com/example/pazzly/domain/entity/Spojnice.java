package com.example.pazzly.domain.entity;

public class Spojnice extends Game{

    public Spojnice(){

    }
    public Spojnice(String gameName, int rounds, int maxPointsPerRound, int minimalPointsPerRound, int durationPerRound) {
        super(gameName, rounds, maxPointsPerRound, minimalPointsPerRound, durationPerRound,1);
    }


}
