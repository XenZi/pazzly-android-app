package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pazzly.R;
import com.example.pazzly.config.SocketHandler;
import com.example.pazzly.domain.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class EntryAppScreenActivity extends Activity {
    private FirebaseFirestore firestore;
    private TextInputLayout txtUsernameLogin;
    private TextInputLayout txtPasswordLogin;
    private Button loginBtn;
    private Button continueAsGuest;
    private Button registerBtn;
    private Socket socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "EntryAppScreen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.entryappscreen);
        SocketHandler.setSocket();
        socket = SocketHandler.getSocket();
        socket.connect();
        socket.on("gameStat", args -> {
            if (args[0] != null) {
                JSONObject data = (JSONObject) args[0];
                try {
                    Integer timer = (Integer) data.get("timer");
                    Log.d("SOCKET TIMER", timer.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        firestore = FirebaseFirestore.getInstance();
        initializeViewElements();

        this.registerBtn.setOnClickListener(v -> {startActivity(new Intent(EntryAppScreenActivity.this, RegisterScreenActivity.class));});
        this.loginBtn.setOnClickListener(v -> proceedLogin());
        this.continueAsGuest.setOnClickListener(v -> {
            startActivity(new Intent(EntryAppScreenActivity.this, GameActivity.class));
        });
    }


    private void initializeViewElements() {
        this.txtUsernameLogin = findViewById(R.id.txtUsernameLogin);
        this.txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.continueAsGuest = findViewById(R.id.asGuestBtn);
        this.registerBtn = findViewById(R.id.registerBtn);
    }

    private void proceedLogin() {
        String username = this.txtUsernameLogin.getEditText().getText().toString();
        String password = this.txtPasswordLogin.getEditText().getText().toString();

        Query query = firestore.collection("users")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        // Access the document fields
                        String id = documentSnapshot.getString("id");
                        String username = documentSnapshot.getString("username");
                        String password = documentSnapshot.getString("password");
                        String email = documentSnapshot.getString("email");
                        Integer tokens = Integer.valueOf(Math.toIntExact(documentSnapshot.getLong("tokens")));
                        Integer stars = Integer.valueOf(Math.toIntExact(documentSnapshot.getLong("stars")));
                        User user = new User(id, username, email, password, tokens, stars);
                        HomeScreenActivity.loggedUser = user;
                        startActivity(new Intent(EntryAppScreenActivity.this, HomeScreenActivity.class));
                    } else {
                        Toast.makeText(EntryAppScreenActivity.this, "No user matching these credentials", Toast.LENGTH_SHORT);
                    }
                } else {
                    // Handle the task failure
                    Exception exception = task.getException();
                }
            }
        });
    }
}
