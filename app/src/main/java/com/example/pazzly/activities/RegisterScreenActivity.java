package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.R;

public class RegisterScreenActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "Register Screen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.register);

        Button button = findViewById(R.id.registerPageRegisterBtn);
        button.setOnClickListener(v -> startActivity(new Intent(RegisterScreenActivity.this, GameActivity.class)));
    }
}
