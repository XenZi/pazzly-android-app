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

public class FragmentGameInfo extends Fragment {

    private int gameDuration;
    private TimerCallback timerCallback;
    private TextView secondsLeftTextView;

    public interface TimerCallback {
        void onTimeTick(int secondsLeft);
        void onTimerFinished();
    }


    public static FragmentGameInfo newInstance(int gameDuration) {
        FragmentGameInfo fragment = new FragmentGameInfo();
        fragment.gameDuration = gameDuration;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fargment_game_info, container, false);
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
        new CountDownTimer(millisecondsDuration, 1000) {
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

}
