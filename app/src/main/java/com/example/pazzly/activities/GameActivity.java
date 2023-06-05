package com.example.pazzly.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;
import com.example.pazzly.domain.entity.Game;
import com.example.pazzly.domain.entity.KorakPoKorak;
import com.example.pazzly.domain.entity.MojBroj;
import com.example.pazzly.domain.model.GameFragmentPair;
import com.example.pazzly.fragments.FragmentGameInfo;
import com.example.pazzly.fragments.FragmentKorakPoKorak;
import com.example.pazzly.fragments.FragmentMojBroj;
import com.example.pazzly.utils.FragmentTransition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements FragmentGameInfo.TimerCallback, FragmentMojBroj.SubmitCallback {
    private Map<Integer, GameFragmentPair> gameFragmentMap = new HashMap<>();
    private boolean gameFinished = false;

    private int currentActiveGame = 0;
    private FragmentGameInfo gameInfoFragment;
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
        gameInfoFragment = FragmentGameInfo.newInstance(gameFragmentMap.get(currentActiveGame).getGame().getDurationPerRound());
        gameInfoFragment.setTimerCallback(this); // Set the callback in the activity
        FragmentTransition.to(gameInfoFragment, this, false, R.id.upView);
    }

    private void initializeGameFragment() {
        Fragment fragmentToInitialize = gameFragmentMap.get(currentActiveGame).getFragment();
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentMojBroj) {
            ((FragmentMojBroj) gameFragmentMap.get(currentActiveGame).getFragment()).setSubmitCallback(this); // Set the callback in the activity
        }
        if (gameFragmentMap.get(currentActiveGame).getGame().getRounds() > 1 && gameFragmentMap.get(currentActiveGame).getGame().getCurrentRound() > 1) {
            switch (currentActiveGame) {
                case 0:
                    fragmentToInitialize = FragmentMojBroj.newInstance();
                    ((FragmentMojBroj) fragmentToInitialize).setSubmitCallback(this);
                    break;
                default:
                    break;
            }
        }
        FragmentTransition.to(fragmentToInitialize, this, false, R.id.downView);
    }

    private void initializeGamesIntoGameList() {
        MojBroj mojBroj = new MojBroj(2, 20, 0, 1);
        Fragment mojBrojFragment = FragmentMojBroj.newInstance();
        KorakPoKorak korakPoKorak = new KorakPoKorak(1, 20, 0, 10);
        FragmentKorakPoKorak fragmentKorakPoKorak = FragmentKorakPoKorak.newInstance();
        GameFragmentPair gameFragmentPairMojBroj = new GameFragmentPair(mojBroj, mojBrojFragment);
        GameFragmentPair gameFragmentPairKorakPoKorak = new GameFragmentPair(korakPoKorak, fragmentKorakPoKorak);
        gameFragmentMap.put(0, gameFragmentPairMojBroj);
        gameFragmentMap.put(1, gameFragmentPairKorakPoKorak);
    }

    @Override
    public void onTimeTick(int secondsLeft) {
        Log.d("GameActivity", "Seconds left: " + secondsLeft);
    }

    @Override
    public void onTimerFinished() {
        Log.d("GameActivity", "Timer finished");
        finishGame();
    }

    private void finishGame() {
        switch (currentActiveGame) {
            case 0: {
                Log.d("CURR-GAME-ACTIVE", String.valueOf(currentActiveGame));
                FragmentMojBroj fragmentMojBroj = (FragmentMojBroj) getSupportFragmentManager().findFragmentById(R.id.downView);
                if (fragmentMojBroj != null) {
                    fragmentMojBroj.handleSubmit();
                    Game currentGame = gameFragmentMap.get(currentActiveGame).getGame();
                    if (currentGame.getCurrentRound() < currentGame.getRounds()) {
                        currentGame.setCurrentRound(currentGame.getCurrentRound() + 1);
                        gameFinished = false;
                    } else {
                        currentActiveGame++;
                    }
                    initializeFragments();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onSubmission() {
        if (!gameFinished) {
            Log.d("TEST GAME FINISHED", "TEST");
            gameFinished = true;
            FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            finishGame();
        }
    }
}
