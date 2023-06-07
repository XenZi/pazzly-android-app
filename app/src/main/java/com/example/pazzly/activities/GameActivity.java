package com.example.pazzly.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;
import com.example.pazzly.domain.entity.Game;
import com.example.pazzly.domain.entity.MojBroj;
import com.example.pazzly.domain.model.GameFragmentPair;
import com.example.pazzly.fragments.FragmentAsocijacije;
import com.example.pazzly.fragments.FragmentGameInfo;
import com.example.pazzly.fragments.FragmentMojBroj;
import com.example.pazzly.fragments.FragmentSkocko;
import com.example.pazzly.utils.FragmentTransition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements FragmentGameInfo.TimerCallback,FragmentAsocijacije.SubmitCallbackAsocijacije {
    private Map<Integer, GameFragmentPair> gameFragmentMap = new HashMap<>();
    private int currentActiveGame = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.game_test);
        initializeGamesIntoGameList();
        initializeFragments();
    }

    private void initializeFragments() {
        initializeGameFragment();
        initializeGameInfoFragment();
    }
    private void initializeGameInfoFragment() {
        FragmentGameInfo gameInfoFragment = FragmentGameInfo.newInstance(gameFragmentMap.get(currentActiveGame).getGame().getDurationPerRound());
        gameInfoFragment.setTimerCallback(this); // Set the callback in the activity
        FragmentTransition.to(gameInfoFragment, this, false, R.id.upView);
    }

    private void initializeGameFragment() {
        FragmentTransition.to(gameFragmentMap.get(currentActiveGame).getFragment(), this, false, R.id.downView);
    }

    private void initializeGamesIntoGameList() {
//        MojBroj mojBroj = new MojBroj(1, 20, 0, 1);
//        Fragment mojBrojFragment = FragmentMojBroj.newInstance();
//        GameFragmentPair gameFragmentPairMojBroj = new GameFragmentPair(mojBroj, mojBrojFragment);
//        gameFragmentMap.put(0, gameFragmentPairMojBroj);

        MojBroj mojBroj = new MojBroj(1, 20, 0, 10);
        Fragment mojBrojFragment = FragmentAsocijacije.newInstance();
        GameFragmentPair gameFragmentPairMojBroj = new GameFragmentPair(mojBroj, mojBrojFragment);
        gameFragmentMap.put(0, gameFragmentPairMojBroj);
    }

    @Override
    public void onTimeTick(int secondsLeft) {
        Log.d("GameActivity", "Seconds left: " + secondsLeft);
    }

    @Override
    public void onTimerFinished() {
        Log.d("GameActivity", "Timer finished");
        switch (currentActiveGame) {
            case 0: {
                FragmentMojBroj fragmentMojBroj = (FragmentMojBroj) getSupportFragmentManager().findFragmentById(R.id.downView);
                if (fragmentMojBroj != null) {
                    fragmentMojBroj.handleSubmit();
                    Game currentGame = gameFragmentMap.get(currentActiveGame).getGame();
                    if (currentGame.getCurrentRound() < currentGame.getRounds()) {
                        initializeFragments();
                    }
                    else {
                        currentActiveGame++;
                    }
                }
            }
        }
    }

    @Override
    public void onSubmissionAsocijacije(int points) {

    }
}
