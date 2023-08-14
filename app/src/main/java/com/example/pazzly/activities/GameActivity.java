package com.example.pazzly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;
import com.example.pazzly.domain.entity.Asocijacija;
import com.example.pazzly.domain.entity.Game;
import com.example.pazzly.domain.entity.KorakPoKorak;
import com.example.pazzly.domain.entity.Match;
import com.example.pazzly.domain.entity.MojBroj;
import com.example.pazzly.domain.entity.Skocko;
import com.example.pazzly.domain.entity.Spojnice;
import com.example.pazzly.domain.model.GameFragmentPair;
import com.example.pazzly.fragments.FragmentAsocijacije;
import com.example.pazzly.fragments.FragmentGameInfo;
import com.example.pazzly.fragments.FragmentKorakPoKorak;
import com.example.pazzly.fragments.FragmentMojBroj;
import com.example.pazzly.fragments.FragmentSkocko;
import com.example.pazzly.fragments.FragmentSpojnice;
import com.example.pazzly.utils.FragmentTransition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameActivity extends AppCompatActivity implements FragmentGameInfo.TimerCallback, FragmentMojBroj.SubmitCallback, FragmentKorakPoKorak.SubmitCallback,FragmentAsocijacije.SubmitCallbackAsocijacije,FragmentSkocko.SubmitCallbackSkocko,FragmentSpojnice.SubmitCallbackSpojnice{
    private Map<Integer, GameFragmentPair> gameFragmentMap = new HashMap<>();
    private boolean gameFinished = false;
    private int firstUserPoints = 0;
    private int currentActiveGame = 0;
    private FragmentGameInfo gameInfoFragment;
    private Match match;
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.game_test);
        this.match = (Match) getIntent().getSerializableExtra("MATCH_KEY");
        initializeGamesIntoGameList();
        initializeFragments();
        this.match.setActiveGame(this.gameFragmentMap.get(currentActiveGame).getGame());
    }

    private void initializeFragments() {
        initializeGameFragment();
        initializeGameInfoFragment();
    }
    private void initializeGameInfoFragment() {
        if (gameFragmentMap.get(currentActiveGame) == null) {
            startActivity(new Intent(GameActivity.this, HomeScreenActivity.class));
            return;
        }
        if (gameInfoFragment == null) {
            gameInfoFragment = FragmentGameInfo.newInstance(gameFragmentMap.get(currentActiveGame).getGame().getDurationPerRound(), this.match);
            gameInfoFragment.setTimerCallback(this); // Set the callback in the activity
            FragmentTransition.to(gameInfoFragment, this, false, R.id.upView);
        } else {
            gameInfoFragment.updatePoints();
            gameInfoFragment.setGameDuration(this.gameFragmentMap.get(currentActiveGame).getGame().getDurationPerRound());
            FragmentTransition.to(gameInfoFragment, this, false, R.id.upView);
        }
    }

    private void initializeGameFragment() {
        if (gameFragmentMap.get(currentActiveGame) == null) {
            startActivity(new Intent(GameActivity.this, HomeScreenActivity.class));
            return;
        }
        Fragment fragmentToInitialize = gameFragmentMap.get(currentActiveGame).getFragment();
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentMojBroj) {
            ((FragmentMojBroj) gameFragmentMap.get(currentActiveGame).getFragment()).setSubmitCallback(this); // Set the callback in the activity
        }
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentKorakPoKorak) {
            ((FragmentKorakPoKorak) gameFragmentMap.get(currentActiveGame).getFragment()).setSubmitCallback(this);
        }
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentAsocijacije) {
            ((FragmentAsocijacije) gameFragmentMap.get(currentActiveGame).getFragment()).setSubmitCallbackAsocijacije(this);
        }
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentSkocko) {
            ((FragmentSkocko) gameFragmentMap.get(currentActiveGame).getFragment()).setSubmitCallbackSkocko(this);
        }
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentSpojnice) {
            ((FragmentSpojnice) gameFragmentMap.get(currentActiveGame).getFragment()).setSubmitCallbackSpojnice(this);
        }
        if (gameFragmentMap.get(currentActiveGame).getGame().getRounds() > 1 && gameFragmentMap.get(currentActiveGame).getGame().getCurrentRound() > 1) {
            switch (currentActiveGame) {
                case 3:
                    fragmentToInitialize = FragmentAsocijacije.newInstance();
                    ((FragmentAsocijacije) fragmentToInitialize).setSubmitCallbackAsocijacije(this);
                    break;
                case 2:
                    fragmentToInitialize = FragmentSkocko.newInstance();
                    ((FragmentSkocko) fragmentToInitialize).setSubmitCallbackSkocko(this);
                    break;
                case 0:
                    fragmentToInitialize = FragmentMojBroj.newInstance(this.match);
                    ((FragmentMojBroj) fragmentToInitialize).setSubmitCallback(this);
                    break;
                case 1:
                    fragmentToInitialize = FragmentKorakPoKorak.newInstance();
                    ((FragmentKorakPoKorak) fragmentToInitialize).setSubmitCallback(this);
                    break;
                case 4:
                    fragmentToInitialize = FragmentSpojnice.newInstance();
                    ((FragmentSpojnice) fragmentToInitialize).setSubmitCallbackSpojnice(this);
                    break;
                default:
                    break;
            }
        }
        FragmentTransition.to(fragmentToInitialize, this, false, R.id.downView);
        gameFinished = false;
    }

    private void initializeGamesIntoGameList() {
        MojBroj mojBroj = new MojBroj(2, 20, 0, 1);
        Fragment mojBrojFragment = FragmentMojBroj.newInstance(this.match);
        KorakPoKorak korakPoKorak = new KorakPoKorak(2, 20, 0, 1);

        FragmentKorakPoKorak fragmentKorakPoKorak = FragmentKorakPoKorak.newInstance();

        GameFragmentPair gameFragmentPairMojBroj = new GameFragmentPair(mojBroj, mojBrojFragment);
        GameFragmentPair gameFragmentPairKorakPoKorak = new GameFragmentPair(korakPoKorak, fragmentKorakPoKorak);
        Asocijacija asocijacija= new Asocijacija(2,30,0,2);
        FragmentAsocijacije fragmentAsocijacije=FragmentAsocijacije.newInstance();
        GameFragmentPair gameFragmentPairAsocijacije=new GameFragmentPair(asocijacija,fragmentAsocijacije);
        Skocko skocko= new Skocko(2,20,0,1);
        FragmentSkocko fragmentSkocko=FragmentSkocko.newInstance();
        GameFragmentPair gameFragmentPairSkocko=new GameFragmentPair(skocko,fragmentSkocko);
        Spojnice spojnice= new Spojnice(2,10,0,1);
        FragmentSpojnice fragmentSpojnice=FragmentSpojnice.newInstance();
        GameFragmentPair gameFragmentPairSpojnice=new GameFragmentPair(spojnice,fragmentSpojnice);

//        gameFragmentMap.put(0,gameFragmentPairAsocijacije);
//        gameFragmentMap.put(1,gameFragmentPairSkocko);
//        gameFragmentMap.put(2, gameFragmentPairKorakPoKorak);
//        gameFragmentMap.put(3, gameFragmentPairMojBroj);
//        gameFragmentMap.put(4,gameFragmentPairSpojnice);

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
        Game currentGame = gameFragmentMap.get(currentActiveGame).getGame();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.downView);
        Log.d("CURRFRAG", "finishGame: " + String.valueOf(currentGame.getCurrentRound() < currentGame.getRounds()));
        if (currentFragment != null) {
            if (currentGame.getCurrentRound() < currentGame.getRounds()) {
                currentGame.setCurrentRound(currentGame.getCurrentRound() + 1);
            } else {
                currentActiveGame++;
            }
            initializeFragments();
            gameFinished = false;
        } else {
            startActivity(new Intent(GameActivity.this, HomeScreenActivity.class));
        }
    }

    @Override
    public void onSubmissionAsocijacije(int points) {
        FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
        firstUserPoints += points;
        fragmentGameInfo.updatePoints();
        if (points>5){
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            finishGame();
        }

    }

    @Override
    public void onSubmissionSpojnice(int points) {
        FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
        firstUserPoints += points;
        fragmentGameInfo.updatePoints();

        if (points==0){
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            finishGame();
        }

    }

    @Override
    public void onSubmissionSkocko(int points) {
        FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
        firstUserPoints += points;
        fragmentGameInfo.updatePoints();

        if (points>0 || points==0){
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            finishGame();
        }

    }

    public void onSubmission(int points) {
        if (!gameFinished) {
            gameFinished = true;
            FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            firstUserPoints += points;
            fragmentGameInfo.updatePoints();
            finishGame();
        }
    }
}
