package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;
import com.example.pazzly.fragments.FragmentGameInfo;
import com.example.pazzly.fragments.FragmentKoZnaZna;
import com.example.pazzly.fragments.FragmentKorakPoKorak;
import com.example.pazzly.fragments.FragmentMojBroj;
import com.example.pazzly.utils.FragmentTransition;

import java.util.Timer;
import java.util.TimerTask;

public class GameTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "GameTest", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.game_test);

        FragmentTransition.to(FragmentGameInfo.newInstance(), this, false, R.id.upView);
        FragmentTransition.to(FragmentMojBroj.newInstance(), this, false, R.id.downView);

        final int SPLASH_TIME_OUT = 5000;
        int i =0;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                GameTest.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransition.to(FragmentKorakPoKorak.newInstance(), GameTest.this, false, R.id.downView);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                FragmentTransition.to(FragmentKoZnaZna.newInstance(), GameTest.this, false, R.id.downView);
                            }
                        }, SPLASH_TIME_OUT);
                    }
                });
            }
        }, SPLASH_TIME_OUT);
    }

    public void mapKoZnaZna() {
        final int SPLASH_TIME_OUT = 10000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                GameTest.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentTransition.to(FragmentKoZnaZna.newInstance(), GameTest.this, false, R.id.downView);
                        finish();
                    }
                });
            }
        }, SPLASH_TIME_OUT);
    }
}
