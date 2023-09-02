package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pazzly.R;
import com.example.pazzly.domain.entity.Match;
import com.example.pazzly.domain.entity.User;
import com.example.pazzly.domain.manager.SocketIOManager;
import com.example.pazzly.domain.model.MatchProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeScreenActivity extends Activity {
    public static User loggedUser;
    private TextView tokenHomeText;
    private Button startGameBtnHome;
    private TextView starsHomeText;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "HomeScreen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.home_screen);
        initializeView();
        this.firestore = FirebaseFirestore.getInstance();
        calculateStarsAndTokensAfterLogin();
        this.tokenHomeText.setText("Tokens: " + loggedUser.getTokens());
        this.starsHomeText.setText("Stars: " + loggedUser.getStars());
        Button button = findViewById(R.id.profileBtn);
        button.setOnClickListener(v -> {startActivity(new Intent(HomeScreenActivity.this, ProfileActivity.class));});
        makeQueueForGameJoin();
    }

    private void calculateStarsAndTokensAfterLogin() {
        int calculatedTokens = loggedUser.getStars() / 50;
        loggedUser.setTokens(loggedUser.getTokens() + calculatedTokens);
        loggedUser.setStars(loggedUser.getStars() - (calculatedTokens * 50));
        Query query = firestore.collection("users")
                .whereEqualTo("id", loggedUser.getId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String lastTokenGiveout = documentSnapshot.getString("lastDayTokenGiven");
                        LocalDateTime timestamp = LocalDateTime.parse(lastTokenGiveout, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        // Get the current date and time
                        LocalDateTime now = LocalDateTime.now();

                        // Calculate the time difference between the timestamp and now
                        Duration duration = Duration.between(timestamp, now);
                        long passedHours = duration.toHours();
                        if (passedHours >= 24) {
                            loggedUser.setTokens(loggedUser.getTokens() + 5);
                            documentSnapshot.getReference().update("lastDayTokenGiven", LocalDateTime.now().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("UPDATE OF 24h", "onComplete: ");
                                }
                            });
                        }
                        documentSnapshot.getReference().update("tokens", loggedUser.getTokens()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("TOKENS UPDATED ON LOGIN", "onComplete: ");
                            }
                        });
                        documentSnapshot.getReference().update("stars", loggedUser.getStars()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("STARS UPDATED ON LOGIN", "onComplete: ");
                            }
                        });
                    }
                }
            }
        });
    }
    private void initializeView() {
        this.tokenHomeText = findViewById(R.id.tokenHomeText);
        this.starsHomeText = findViewById(R.id.stars);
        this.startGameBtnHome = findViewById(R.id.startGameBtnHome);
    }

    private void makeQueueForGameJoin() {
        if (loggedUser.getTokens() == 0) {
            return;
        }
        this.startGameBtnHome.setOnClickListener(v -> {
            SocketIOManager.getInstance().connect();
            try {
                SocketIOManager.getInstance().getSocket().emit("joinQueue", formatJSONObject());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            SocketIOManager.getInstance().getSocket().on("startMatch", (args) -> {
                JSONObject firstObject = (JSONObject) args[0];
                try {
                    String id = (String) firstObject.get("id");
                    JSONObject nestedPlayer1Object = (JSONObject) firstObject.get("player1");
                    MatchProfile player1 = new MatchProfile(nestedPlayer1Object.getString("id"), nestedPlayer1Object.getInt("points"), nestedPlayer1Object.getString("username"), nestedPlayer1Object.getString("image"));
                    JSONObject nestedPlayer2Object = (JSONObject) firstObject.get("player2");
                    MatchProfile player2 = new MatchProfile(nestedPlayer2Object.getString("id"), nestedPlayer2Object.getInt("points"), nestedPlayer2Object.getString("username"), nestedPlayer2Object.getString("image"));
                    String playerTurn = firstObject.getString("turn");
                    Match match = new Match(id, player1, player2, playerTurn, null);
                    loggedUser.setTokens(loggedUser.getTokens() - 1);
                    Query query = firestore.collection("users")
                            .whereEqualTo("id", loggedUser.getId());
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                documentSnapshot.getReference().update("tokens", loggedUser.getTokens()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("gagagaga", "onComplete: ");
                                    }
                                });
                            }
                        }
                    });
                    Intent intent = new Intent(HomeScreenActivity.this, GameActivity.class);
                    intent.putExtra("MATCH_KEY", match);
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    private JSONObject formatJSONObject() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("id", loggedUser.getId());
        data.put("points", 0);
        data.put("username", loggedUser.getUsername());
        data.put("image", "");
        return data;
    }
    private MatchProfile createMatchProfile() {
        MatchProfile matchProfile = new MatchProfile();
        matchProfile.setUUID(loggedUser.getId());
        matchProfile.setPoints(0);
        matchProfile.setUsername(loggedUser.getUsername());
        matchProfile.setImage("");
        return matchProfile;
    }
}
