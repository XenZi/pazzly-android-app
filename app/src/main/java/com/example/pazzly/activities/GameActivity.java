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
import com.example.pazzly.domain.entity.KoZnaZna;
import com.example.pazzly.domain.entity.KorakPoKorak;
import com.example.pazzly.domain.entity.Match;
import com.example.pazzly.domain.entity.MojBroj;
import com.example.pazzly.domain.entity.Skocko;
import com.example.pazzly.domain.entity.Spojnice;
import com.example.pazzly.domain.model.GameFragmentPair;
import com.example.pazzly.domain.model.MatchProfile;
import com.example.pazzly.fragments.FragmentAsocijacije;
import com.example.pazzly.fragments.FragmentGameInfo;
import com.example.pazzly.fragments.FragmentKoZnaZna;
import com.example.pazzly.fragments.FragmentKorakPoKorak;
import com.example.pazzly.fragments.FragmentMojBroj;
import com.example.pazzly.fragments.FragmentSkocko;
import com.example.pazzly.fragments.FragmentSpojnice;
import com.example.pazzly.utils.FragmentTransition;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameActivity extends AppCompatActivity implements FragmentGameInfo.TimerCallback, FragmentMojBroj.SubmitCallbackForMojBroj ,FragmentAsocijacije.SubmitCallbackAsocijacije,FragmentSkocko.SubmitCallbackSkocko,FragmentSpojnice.SubmitCallbackSpojnice, FragmentKorakPoKorak.SubmitCallbackKorakPoKorak, FragmentKoZnaZna.SubmitCallbackKoZnaZna {
    private Map<Integer, GameFragmentPair> gameFragmentMap = new HashMap<>();
    private boolean gameFinished = false;
    private int currentActiveGame = 0;
    private FragmentGameInfo gameInfoFragment;
    private Match match;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.game_test);
        firestore = FirebaseFirestore.getInstance();
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
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentKoZnaZna) {
            ((FragmentKoZnaZna) gameFragmentMap.get(currentActiveGame).getFragment()).setCallbackKoZnaZna(this);
        }
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentMojBroj) {
            ((FragmentMojBroj) gameFragmentMap.get(currentActiveGame).getFragment()).setSubmitCallbackForMojBroj(this); // Set the callback in the activity
        }
        if (gameFragmentMap.get(currentActiveGame).getFragment() instanceof FragmentKorakPoKorak) {
            ((FragmentKorakPoKorak) gameFragmentMap.get(currentActiveGame).getFragment()).setCallbackKorakPoKorak(this);
        }

        if (gameFragmentMap.get(currentActiveGame).getGame().getRounds() > 1 && gameFragmentMap.get(currentActiveGame).getGame().getCurrentRound() > 1) {
            switch (currentActiveGame) {
                case 0:
                    fragmentToInitialize = FragmentKoZnaZna.newInstance(this.match);
                    ((FragmentKoZnaZna) fragmentToInitialize).setCallbackKoZnaZna(this);
                    break;
                case 1:
                    fragmentToInitialize = FragmentKorakPoKorak.newInstance(this.match);
                    ((FragmentKorakPoKorak) fragmentToInitialize).setCallbackKorakPoKorak(this);
                    break;
                case 2:
                    fragmentToInitialize = FragmentMojBroj.newInstance(this.match);
                    ((FragmentMojBroj) fragmentToInitialize).setSubmitCallbackForMojBroj(this);
                    break;
                default:
                    break;
            }
        }
        FragmentTransition.to(fragmentToInitialize, this, false, R.id.downView);
        gameFinished = false;
    }

    private void initializeGamesIntoGameList() {
        KoZnaZna koZnaZna = new KoZnaZna("Ko Zna Zna", 1, 50, -25, 0.06);
        FragmentKoZnaZna fragmentKoZnaZna = FragmentKoZnaZna.newInstance(this.match);
        GameFragmentPair gameFragmentPairKoZnaZna = new GameFragmentPair(koZnaZna, fragmentKoZnaZna);
        MojBroj mojBroj = new MojBroj("Moj Broj", 2, 20, 0, 1);
        Fragment mojBrojFragment = FragmentMojBroj.newInstance(this.match);
        KorakPoKorak korakPoKorak = new KorakPoKorak("Korak Po Korak", 2, 20, 0, 1);
        FragmentKorakPoKorak fragmentKorakPoKorak = FragmentKorakPoKorak.newInstance(this.match);
        GameFragmentPair gameFragmentPairMojBroj = new GameFragmentPair(mojBroj, mojBrojFragment);
        GameFragmentPair gameFragmentPairKorakPoKorak = new GameFragmentPair(korakPoKorak, fragmentKorakPoKorak);

        gameFragmentMap.put(0, gameFragmentPairKoZnaZna);
        gameFragmentMap.put(1, gameFragmentPairKorakPoKorak);
        gameFragmentMap.put(2, gameFragmentPairMojBroj);
    }

    @Override
    public void onTimeTick(int secondsLeft) {
    }

    @Override
    public void onTimerFinished() {
        finishGame();
    }

    private void finishGame() {
        Game currentGame = gameFragmentMap.get(currentActiveGame).getGame();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.downView);
        FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);

        if (currentActiveGame == 3) {
            fragmentGameInfo.stopTimer();
            starsCount();
            startActivity(new Intent(GameActivity.this, HomeScreenActivity.class));
            finish();
            return;
        }

        if (currentFragment == null) {
            starsCount();
            startActivity(new Intent(GameActivity.this, HomeScreenActivity.class));
            return;
        }
        if (currentGame.getCurrentRound() < currentGame.getRounds()) {
            // Continue to the next round of the current game
            currentGame.setCurrentRound(currentGame.getCurrentRound() + 1);
            this.match.setPlayerTurn(this.match.getPlayer2().getUUID());
            Log.d("finishGame", "Moving to next round");
        } else if (currentActiveGame < gameFragmentMap.size() - 1) {
            // Move to the next game type
            currentActiveGame++;
            this.match.setPlayerTurn(this.match.getPlayer1().getUUID());
        } else {
            // All games are finished, navigate to home screen
            starsCount();
            startActivity(new Intent(GameActivity.this, HomeScreenActivity.class));
        }
        initializeFragments();
        gameFinished = false;


    }


    private void starsCount() {
        String userIdWhoWonTheGame = this.match.getPlayer1().getPoints() > this.match.getPlayer2().getPoints() ? this.match.getPlayer1().getUUID() : this.match.getPlayer2().getUUID();
        MatchProfile matchProfile = this.match.getPlayer1().getUUID() == HomeScreenActivity.loggedUser.getId() ? this.match.getPlayer1() : this.match.getPlayer2();;
        int stars = 0;
        if (userIdWhoWonTheGame.equals(HomeScreenActivity.loggedUser.getId())) {
            stars += 10;
        } else {
            stars -= 10;
        }
        stars += matchProfile.getPoints() / 40;
        Log.d("STARS", "starsCount: " + stars);
    }

    @Override
    public void onSubmissionAsocijacije(int points) {
        FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
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
        fragmentGameInfo.updatePoints();

        if (points>0 || points==0){
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            finishGame();
        }

    }

    @Override
    public void onSubmissionKorakPoKorak() {
        if (!gameFinished) {
            gameFinished = true;
            FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            fragmentGameInfo.updatePoints();
            finishGame();
        }
    }

    @Override
    public void onSubmissionKoZnaZna() {
        if (!gameFinished) {
            gameFinished = true;
            FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            fragmentGameInfo.updatePoints();
            finishGame();
        }
    }

    @Override
    public void updateKoZnaZnaPoints() {
        if (!gameFinished) {
            FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
            fragmentGameInfo.updatePoints();
        }
    }

    @Override
    public void onSubmissionMojBroj() {
        if (!gameFinished) {
            gameFinished = true;
            FragmentGameInfo fragmentGameInfo = (FragmentGameInfo) getSupportFragmentManager().findFragmentById(R.id.upView);
            if (fragmentGameInfo != null) {
                fragmentGameInfo.stopTimer();
            }
            fragmentGameInfo.updatePoints();
            finishGame();
        }
    }

    private void formatForMatchStoreInTheFirebase() {

    }
}

