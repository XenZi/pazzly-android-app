package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.R;

public class HomeScreenActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "HomeScreen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.home_screen);
        Button button = findViewById(R.id.profileBtn);



        button.setOnClickListener(v -> {startActivity(new Intent(HomeScreenActivity.this, ProfileActivity.class));});


    }
}
