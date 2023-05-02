package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.R;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.profile);



    }
}
