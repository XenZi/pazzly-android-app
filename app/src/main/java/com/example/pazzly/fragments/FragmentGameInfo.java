package com.example.pazzly.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.example.pazzly.R;
import com.example.pazzly.domain.entity.Match;

public class FragmentGameInfo extends Fragment {

    private int gameDuration;
    private View view;
    private TimerCallback timerCallback;
    private TextView secondsLeftTextView;
    private TextView firstUserUsername;
    private TextView secondUserUsername;
    private TextView secondUserPointsTextView;
    private CountDownTimer timer;
    private TextView firstUserPointsTextView;
    private int firstUserPoints;

    private Match match;

    public void setFirstUserPoints(int firstUserPoints) {
        this.firstUserPoints = firstUserPoints;
    }

    public interface TimerCallback {
        void onTimeTick(int secondsLeft);
        void onTimerFinished();
    }


    public static FragmentGameInfo newInstance(int gameDuration, Match match) {
        FragmentGameInfo fragment = new FragmentGameInfo();
        fragment.gameDuration = gameDuration;
        fragment.match = match;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fargment_game_info, container, false);
        firstUserPointsTextView = view.findViewById(R.id.firstUserPoints);
        firstUserPointsTextView.setText(String.valueOf(firstUserPoints));
        firstUserUsername = view.findViewById(R.id.firstUserUsername);
        firstUserUsername.setText(String.valueOf(match.getPlayer1().getUsername()));
        secondUserUsername = view.findViewById(R.id.secondUserUsername);
        secondUserUsername.setText(String.valueOf(match.getPlayer2().getUsername()));
        secondUserPointsTextView = view.findViewById(R.id.secondUserPoints);
        secondUserPointsTextView.setText(String.valueOf(match.getPlayer2().getPoints()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        secondsLeftTextView = view.findViewById(R.id.secondsLeft);
        startTimer(gameDuration);
    }

    public void setTimerCallback(TimerCallback callback) {
        timerCallback = callback;
    }

    private void startTimer(int duration) {
        long millisecondsDuration = duration * 60 * 1000; // Convert minutes to milliseconds

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timer = new CountDownTimer(millisecondsDuration, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            int secondsLeft = (int) (millisUntilFinished / 1000);
                            if (timerCallback != null) {
                                timerCallback.onTimeTick(secondsLeft);
                            }
                            if (secondsLeftTextView != null) {
                                secondsLeftTextView.setText(String.valueOf(secondsLeft));
                            }
                        }

                        @Override
                        public void onFinish() {
                            if (timerCallback != null) {
                                timerCallback.onTimerFinished();
                            }
                        }
                    }.start();
                }
            });
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void updatePoints() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                firstUserPointsTextView.setText(String.valueOf(match.getPlayer1().getPoints()));
                secondUserPointsTextView.setText(String.valueOf(match.getPlayer2().getPoints()));
            }
        });
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
        startTimer(gameDuration);
    }
}
