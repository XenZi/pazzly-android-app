package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.R;
import com.example.pazzly.domain.entity.User;

public class HomeScreenActivity extends Activity {
    static User loggedUser;
    private TextView tokenHomeText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "HomeScreen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.home_screen);
        initializeView();
        this.tokenHomeText.setText("Tokens: " + loggedUser.getTokens());
        Button button = findViewById(R.id.profileBtn);
        button.setOnClickListener(v -> {startActivity(new Intent(HomeScreenActivity.this, ProfileActivity.class));});
    }

    private void initializeView() {
        this.tokenHomeText = findViewById(R.id.tokenHomeText);
    }
}
