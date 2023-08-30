package com.example.pazzly.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;
import com.example.pazzly.activities.HomeScreenActivity;
import com.example.pazzly.domain.entity.Match;
import com.example.pazzly.domain.manager.SocketIOManager;
import com.example.pazzly.domain.model.KoZnaZnaQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentKoZnaZna extends Fragment {

    private View view;
    private TextView questionText;
    private List<KoZnaZnaQuestion> koZnaZnaQuestions;
    private List<Button> optionButtons;
    private Match match;
    private int currentRound = 0;
    private SubmitCallbackKoZnaZna callbackKoZnaZna;
    private long questionShowTime;
    private Handler questionChangeHandler;
    private Runnable questionChangeRunnable;

    public interface SubmitCallbackKoZnaZna {
        void onSubmissionKoZnaZna();
        void updateKoZnaZnaPoints();
    }

    public SubmitCallbackKoZnaZna getCallbackKoZnaZna() {
        return callbackKoZnaZna;
    }

    public void setCallbackKoZnaZna(SubmitCallbackKoZnaZna callbackKoZnaZna) {
        this.callbackKoZnaZna = callbackKoZnaZna;
    }

    public static FragmentKoZnaZna newInstance(Match match) {
        FragmentKoZnaZna fragmentKoZnaZna = new FragmentKoZnaZna();
        fragmentKoZnaZna.match = match;
        return fragmentKoZnaZna;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ko_zna_zna, container, false);
        initializeViews();
        try {
            initializeSocketInitialKoZnaZnaConnection();
            initializeSocketKoZnaZnaQuestionsConnection();
            initializeButtonClickListener();
            koZnaZnaMatchUpdate();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        questionChangeHandler = new Handler(Looper.getMainLooper());
        questionChangeRunnable = this::changeQuestionAndUpdateUI;
        startQuestionChanging();
        return view;
    }
    private void startQuestionChanging() {
        questionChangeHandler.postDelayed(questionChangeRunnable, 5000);
    }

    private void changeQuestionAndUpdateUI() {
//        currentRound = (currentRound + 1) % koZnaZnaQuestions.size();
//        initializeQuestionIntoView();
//        initializeOptionsFromList();
//        startQuestionChanging();
        currentRound = (currentRound + 1) % koZnaZnaQuestions.size();
        initializeQuestionIntoView();
        initializeOptionsFromList();

        if (currentRound == 0) {
            // All rounds completed, call the submission callback method
            callbackKoZnaZna.onSubmissionKoZnaZna();
        }

        // If not the last round, start the timer again
        if (currentRound < koZnaZnaQuestions.size() - 1) {
            startQuestionChanging();
        }
    }

    private void initializeViews() {
        questionText = view.findViewById(R.id.questionText);
        optionButtons = new ArrayList<>();
        optionButtons.add(view.findViewById(R.id.btnOption1));
        optionButtons.add(view.findViewById(R.id.btnOption2));
        optionButtons.add(view.findViewById(R.id.btnOption3));
        optionButtons.add(view.findViewById(R.id.btnOption4));
    }

    private void initializeSocketInitialKoZnaZnaConnection() throws JSONException {
     SocketIOManager.getInstance().getSocket().emit("koZnaZnaInitialStart", new JSONObject().put("matchId", this.match.getId()));
    }

    private void initializeSocketKoZnaZnaQuestionsConnection() {
        SocketIOManager.getInstance().getSocket().on("koZnaZnaQuestions", args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            koZnaZnaQuestions = new ArrayList<>();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++) {
                    koZnaZnaQuestions.add(castToQuestionFromJSONObject(jsonArray.getJSONObject(i)));
                }
                initializeQuestionIntoView();
                initializeOptionsFromList();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private KoZnaZnaQuestion castToQuestionFromJSONObject(JSONObject jsonObject) {
        try {
            String pitanje = jsonObject.getString("pitanje");
            String tacanOdgovor = jsonObject.getString("tacanOdgovor");
            JSONArray jsonArray = jsonObject.getJSONArray("odgovori");
            List<String> odgovori = new ArrayList<>();
            for (int i=0; i < jsonArray.length(); i++) {
                odgovori.add(jsonArray.getString(i));
            }
            KoZnaZnaQuestion koZnaZnaQuestion = new KoZnaZnaQuestion();
            koZnaZnaQuestion.setQuestion(pitanje);
            koZnaZnaQuestion.setCorrectAnswer(tacanOdgovor);
            koZnaZnaQuestion.setOptions(odgovori);
            return koZnaZnaQuestion;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeQuestionIntoView() {
        questionShowTime = System.currentTimeMillis();
        this.questionText.setText(koZnaZnaQuestions.get(currentRound).getQuestion());
    }
    private void initializeOptionsFromList() {
        for (int i=0;i<optionButtons.size();i++) {
            optionButtons.get(i).setText(koZnaZnaQuestions.get(currentRound).getOptions().get(i));
        }
    }

    private void initializeButtonClickListener() {
        optionButtons.forEach(button -> {
            button.setOnClickListener(click -> {
                try {
                    handleButtonSubmit(button, button.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    private void handleButtonSubmit(Button button, String clickedAnswer) throws JSONException {
        long responseTime = System.currentTimeMillis() - questionShowTime;
        long seconds = responseTime / 1000;
        boolean isAnswerCorrect = koZnaZnaQuestions.get(currentRound).getCorrectAnswer().equals(clickedAnswer);

        SocketIOManager.getInstance().getSocket().emit("koZnaZnaAnswer", new JSONObject().put("matchId", this.match.getId()).put("player", HomeScreenActivity.loggedUser.getId()).put("time", seconds).put("isCorrect", isAnswerCorrect));
        if (koZnaZnaQuestions.get(currentRound).getCorrectAnswer().equals(clickedAnswer)) {
            button.setBackgroundColor(Color.rgb(0, 128, 0));
        } else {
            button.setBackgroundColor(Color.rgb(255, 0, 0));
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            button.setBackgroundColor(Color.parseColor("#72BBD3"));
        }, 1000);
    }

    private void koZnaZnaMatchUpdate() {
        SocketIOManager.getInstance().getSocket().on("koZnaZnaMatchUpdate", args -> {
           JSONObject jsonObject = (JSONObject) args[0];
            try {
                int player1Points = jsonObject.getInt("player1Points");
                int player2Points = jsonObject.getInt("player2Points");

                this.match.getPlayer1().setPoints(player1Points);
                this.match.getPlayer2().setPoints(player2Points);
                callbackKoZnaZna.updateKoZnaZnaPoints();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
