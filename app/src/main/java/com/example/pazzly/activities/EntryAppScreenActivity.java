package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.R;
import com.example.pazzly.config.SocketHandler;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class EntryAppScreenActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "EntryAppScreen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.entryappscreen);
        Button button = findViewById(R.id.registerBtn);
        Button logIn=findViewById(R.id.loginBtn);
        Button continueAsGuest = findViewById(R.id.asGuestBtn);
        button.setOnClickListener(v -> {startActivity(new Intent(EntryAppScreenActivity.this, RegisterScreenActivity.class));});
        logIn.setOnClickListener(v -> {startActivity(new Intent(EntryAppScreenActivity.this, HomeScreenActivity.class));});
        continueAsGuest.setOnClickListener(v -> {
            startActivity(new Intent(EntryAppScreenActivity.this, GameActivity.class));
        });
    }
}
