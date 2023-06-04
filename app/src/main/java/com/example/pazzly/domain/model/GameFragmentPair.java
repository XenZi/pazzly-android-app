package com.example.pazzly.domain.model;

import androidx.fragment.app.Fragment;

import com.example.pazzly.domain.entity.Game;

public class GameFragmentPair {
    private Game game;
    private Fragment fragment;

    public GameFragmentPair() {
    }

    public GameFragmentPair(Game game, Fragment fragment) {
        this.game = game;
        this.fragment = fragment;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
