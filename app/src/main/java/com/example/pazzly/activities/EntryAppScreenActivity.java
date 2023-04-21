package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.MainActivity;
import com.example.pazzly.R;

import java.util.Timer;
import java.util.TimerTask;

public class EntryAppScreenActivity extends Activity {
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "EntryAppScreen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.entryappscreen);
        Button button = findViewById(R.id.registerBtn);

        button.setOnClickListener(v -> {startActivity(new Intent(EntryAppScreenActivity.this, RegisterScreenActivity.class));});
    }


}
