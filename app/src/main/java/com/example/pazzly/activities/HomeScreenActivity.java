package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.R;
import com.example.pazzly.domain.entity.Match;
import com.example.pazzly.domain.entity.User;
import com.example.pazzly.domain.manager.SocketIOManager;
import com.example.pazzly.domain.model.MatchProfile;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeScreenActivity extends Activity {
    public static User loggedUser;
    private TextView tokenHomeText;
    private Button startGameBtnHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "HomeScreen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.home_screen);
        initializeView();
        this.tokenHomeText.setText("Tokens: " + loggedUser.getTokens());
        Button button = findViewById(R.id.profileBtn);
        button.setOnClickListener(v -> {startActivity(new Intent(HomeScreenActivity.this, ProfileActivity.class));});
        makeQueueForGameJoin();
    }

    private void initializeView() {
        this.tokenHomeText = findViewById(R.id.tokenHomeText);
        this.startGameBtnHome = findViewById(R.id.startGameBtnHome);
    }

    private void makeQueueForGameJoin() {
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
