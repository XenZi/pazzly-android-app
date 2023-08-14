package com.example.pazzly.domain.entity;

import android.os.Parcelable;

import com.example.pazzly.domain.model.GameInfo;
import com.example.pazzly.domain.model.MatchProfile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match implements Serializable {
    private String id;
    private MatchProfile player1;
    private MatchProfile player2;
    private String playerTurn;
    private Game activeGame;

    public Match(String id, MatchProfile player1, MatchProfile player2, String playerTurn, Game activeGame) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.playerTurn = playerTurn;
        this.activeGame = activeGame;
    }

    public Match() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MatchProfile getPlayer1() {
        return player1;
    }

    public void setPlayer1(MatchProfile player1) {
        this.player1 = player1;
    }

    public MatchProfile getPlayer2() {
        return player2;
    }

    public void setPlayer2(MatchProfile player2) {
        this.player2 = player2;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Game getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(Game activeGame) {
        this.activeGame = activeGame;
    }
}
