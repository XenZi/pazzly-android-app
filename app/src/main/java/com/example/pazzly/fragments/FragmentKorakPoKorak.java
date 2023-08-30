package com.example.pazzly.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;
import com.example.pazzly.activities.HomeScreenActivity;
import com.example.pazzly.domain.entity.Match;
import com.example.pazzly.domain.manager.SocketIOManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentKorakPoKorak extends Fragment {
    private View view;
    private Handler handler;
    private FirebaseFirestore firestoreDB;
    private int currentStep = 0;
    private List<TextView> steps;
    private List<String> valueSteps;
    private Button submitButton;
    private String konacniOdgovor;
    private Match match;
    private SubmitCallbackKorakPoKorak callbackKorakPoKorak;
    public static FragmentKorakPoKorak newInstance(Match match) {
        FragmentKorakPoKorak fragmentKorakPoKorak = new FragmentKorakPoKorak();
        fragmentKorakPoKorak.setMatch(match);
        return fragmentKorakPoKorak;
    }


    public SubmitCallbackKorakPoKorak getCallbackKorakPoKorak() {
        return callbackKorakPoKorak;
    }

    public void setCallbackKorakPoKorak(SubmitCallbackKorakPoKorak callbackKorakPoKorak) {
        this.callbackKorakPoKorak = callbackKorakPoKorak;
    }

    public interface SubmitCallbackKorakPoKorak {
        void onSubmissionKorakPoKorak();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_korak_po_korak, container, false);
        initializeStepsTextView();
        initializeView();
        firestoreDB = FirebaseFirestore.getInstance();
        handler = new Handler();

        if (!this.match.getPlayerTurn().equals(HomeScreenActivity.loggedUser.getId())) {
            freezeFragment();
        } else {
            try {
                SocketIOManager.getInstance().getSocket().emit("korakPoKorakInitialStart", new JSONObject().put("matchId", this.match.getId()));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        initializeStepsListWithStringValues();
        initializeSubmitFinalAnswerListenerSocketAction();
        initializeEndGameSocket();
        return view;
    }

    private void initializeView() {
        submitButton = view.findViewById(R.id.submitKorakPoKorakAnswer);
        submitButton.setOnClickListener(click -> {
            try {
                handleSubmit();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void initializeStepsTextView() {
        steps = new ArrayList<>();
        steps.add(view.findViewById(R.id.step1));
        steps.add(view.findViewById(R.id.step2));
        steps.add(view.findViewById(R.id.step3));
        steps.add(view.findViewById(R.id.step4));
        steps.add(view.findViewById(R.id.step5));
        steps.add(view.findViewById(R.id.step6));
        steps.add(view.findViewById(R.id.step7));
    }

    private void initializeStepsListWithStringValues() {
        valueSteps = new ArrayList<>();
        konacniOdgovor = "";
        SocketIOManager.getInstance().getSocket().on("korakPoKorakValues", args -> {
           JSONObject object = (JSONObject) args[0];
            try {
                konacniOdgovor = object.getString("finalno");
                JSONArray jsonArray = (JSONArray) object.getJSONArray("koraci");
                for (int i=0;i<jsonArray.length();i++) {
                    valueSteps.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            startUpdatingSteps();
        });
    }

    private void startUpdatingSteps() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateStepText();
                currentStep++;
                if (currentStep > 6) {
                    stopUpdatingSteps();
                    return;
                }
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }
    private void stopUpdatingSteps() {
        handler.removeCallbacksAndMessages(null);
    }


    private void updateStepText() {
        if (currentStep > 6) {
            stopUpdatingSteps();
            return;
        }
        for (int i = 0; i < steps.size(); i++) {
            if (i > currentStep) {
                steps.get(i).setText("");
            }
        }
        steps.get(currentStep).setText(valueSteps.get(currentStep));
    }

    public void handleSubmit() throws JSONException {
        TextInputLayout textInputLayout = view.findViewById(R.id.korakPoKorakAnswer);
        EditText editText = textInputLayout.getEditText();
        String passedAnswer = editText.getText().toString();
        if (!passedAnswer.equals(konacniOdgovor)) {
            initializeSubmitFinalInvalidAnswerSocketAction();
            return;
        }
        initializeSubmitFinalAnswerSocketAction();
    }

    private void freezeFragment() {
        submitButton.setEnabled(false);
        TextInputLayout textInputLayout = view.findViewById(R.id.korakPoKorakAnswer);
        EditText editText = textInputLayout.getEditText();
        editText.setEnabled(false);
    }

    private void unfreezeFragment() {
        submitButton.setEnabled(true);
        TextInputLayout textInputLayout = view.findViewById(R.id.korakPoKorakAnswer);
        EditText editText = textInputLayout.getEditText();
        editText.setEnabled(true);

    }

    private void initializeSubmitFinalAnswerSocketAction() throws JSONException {
        TextInputLayout textInputLayout = view.findViewById(R.id.korakPoKorakAnswer);
        EditText editText = textInputLayout.getEditText();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("matchId", this.match.getId());
        jsonObject.put("playerTurnId", this.match.getPlayerTurn());
        jsonObject.put("step", this.currentStep == 0 ? 0 : this.currentStep - 1);
        jsonObject.put("answer", editText.getText());
        jsonObject.put("hasMoreRounds", this.match.getActiveGame().getCurrentRound() < this.match.getActiveGame().getRounds());
        SocketIOManager.getInstance().getSocket().emit("submitFinalAnswerForKorakPoKorakCorrect", jsonObject);

    }
    private void initializeSubmitFinalAnswerListenerSocketAction() {
        SocketIOManager.getInstance().getSocket().on("korakPoKorakSubmitedAnswer", args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            String submitedAnswer = null;
            try {
                submitedAnswer = jsonObject.getString("answer");
                TextInputLayout textInputLayout = view.findViewById(R.id.korakPoKorakAnswer);
                String finalSubmitedAnswer = submitedAnswer;
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textInputLayout.getEditText().setText(finalSubmitedAnswer);
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                textInputLayout.getEditText().setText("");
                            }, 1500);
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void initializeSubmitFinalInvalidAnswerSocketAction() throws JSONException {
        TextInputLayout textInputLayout = view.findViewById(R.id.korakPoKorakAnswer);
        EditText editText = textInputLayout.getEditText();
        SocketIOManager.getInstance().getSocket().emit("submitFinalAnswerForKorakPoKorakInvalidAnswer", new JSONObject().put("matchId", this.getMatch().getId()).put("answer", editText.getText()));
    }

    private void initializeEndGameSocket() {
            SocketIOManager.getInstance().getSocket().on("endGameKorakPoKorak", args -> {
                JSONObject jsonObject = (JSONObject) args[0];
                try {
                    Log.d("ENDGAMEKORAKPOKORAK", "initializeEndGameSocket: ");
                    int player1Points = jsonObject.getInt("player1Points");
                    int player2Points = jsonObject.getInt("player2Points");
                    this.match.getPlayer1().setPoints(player1Points);
                    this.match.getPlayer2().setPoints(player2Points);
                    callbackKorakPoKorak.onSubmissionKorakPoKorak();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
    }
    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
