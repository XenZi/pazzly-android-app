package com.example.pazzly.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pazzly.R;
import com.example.pazzly.fragments.FragmentGameInfo;
import com.example.pazzly.utils.FragmentTransition;

public class GameTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "GameTest", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.game_test);

        FragmentTransition.to(FragmentGameInfo.newInstance(), this, false, R.id.fragmentContainerView2);

    }
}
