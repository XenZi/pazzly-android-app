package com.example.pazzly.fragments;

import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import com.example.pazzly.MainActivity;
import com.example.pazzly.R;
import com.example.pazzly.activities.GameActivity;
import com.example.pazzly.activities.HomeScreenActivity;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import com.example.pazzly.activities.HomeScreenActivity;
import com.example.pazzly.domain.entity.Game;
import com.example.pazzly.domain.entity.Match;
import com.example.pazzly.domain.manager.SocketIOManager;

public class FragmentMojBroj extends Fragment {
    private View view;
    private TextView wantedNumberTextView;
    private Button stopButton;
    private Button deleteButton;
    private Button submitButton;
    private Handler handler;
    private Random random;
    private boolean isUpdating;
    private AtomicInteger clickCounter;
    private TextView currentStateOfExpression;
    private List<Button> numberButtons;
    private List<Button> operationButtons;
    private SubmitCallback submitCallback;
    private ConstraintLayout wholeFragment;
    private TextView resultFromUser1;
    private TextView resultFromUser2;
    private Match match;
    private GameActivity gameActivity;
    public static FragmentMojBroj newInstance(Match match) {
        FragmentMojBroj fragmentMojBroj = new FragmentMojBroj();
        fragmentMojBroj.setMatch(match);
        return fragmentMojBroj;
    }

    public interface SubmitCallback {
        void onSubmission(int points) throws JSONException;
    }

    public void setSubmitCallback(SubmitCallback callback) {
        submitCallback = callback;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moj_broj, container, false);
        this.gameActivity = (GameActivity) getActivity();
        initializeViews();
        initializeNumberButtons();
        initializeOperationButtons();
        initializeListeners();
        initializeValues();

        handler = new Handler();
        random = new Random();
        isUpdating = true;
        clickCounter = new AtomicInteger();

        handler.post(updateRunnableForFinalNumber);
        initializeSocketListenerForRandomNumber();
        if (!HomeScreenActivity.loggedUser.getId().equals(this.match.getPlayerTurn())) {
            freezeScreen();
        }
        gameResultDone();
        return view;
    }


    private void initializeSocketListenerForRandomNumber() {
        SocketIOManager.getInstance().getSocket().on("sendRandomNumber", args -> {
            JSONObject object = (JSONObject) args[0];
            try {
                String number = object.getString("randomNumber");
                JSONArray array = (JSONArray) object.getJSONArray("selectedNumbers");
                isUpdating = false;
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wantedNumberTextView.setText(number);
                            for (int i = 0;i< array.length();i++) {
                                try {
                                    numberButtons.get(i).setText(String.valueOf(array.get(i)));
                                    unfreezeScreen();

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializeViews() {
        wholeFragment = view.findViewById(R.id.mojbrojFragment);
        wantedNumberTextView = view.findViewById(R.id.wantedNumber);
        stopButton = view.findViewById(R.id.stopMojBroj);
        deleteButton = view.findViewById(R.id.deleteExpressionButton);
        submitButton = view.findViewById(R.id.btnSubmitMojBroj);
        currentStateOfExpression = view.findViewById(R.id.currentStateOfExpression);
        resultFromUser1 = view.findViewById(R.id.resultFromUser1);
        resultFromUser2 = view.findViewById(R.id.resultFromUser2);
    }

    private void initializeNumberButtons() {
        numberButtons = new ArrayList<>();
        numberButtons.add(view.findViewById(R.id.randomNr1));
        numberButtons.add(view.findViewById(R.id.randomNr2));
        numberButtons.add(view.findViewById(R.id.randomNr3));
        numberButtons.add(view.findViewById(R.id.randomNr4));
        numberButtons.add(view.findViewById(R.id.randomNr5));
        numberButtons.add(view.findViewById(R.id.randomNr6));
    }

    private void initializeOperationButtons() {
        operationButtons = new ArrayList<>();
        operationButtons.add(view.findViewById(R.id.opAdd));
        operationButtons.add(view.findViewById(R.id.opSubtract));
        operationButtons.add(view.findViewById(R.id.opMultiply));
        operationButtons.add(view.findViewById(R.id.opDivide));
        operationButtons.add(view.findViewById(R.id.opOpenBracket));
        operationButtons.add(view.findViewById(R.id.opClosedBracket));
    }

    private void initializeListeners() {
        stopButton.setOnClickListener(v -> handleStopButtonClick());
        deleteButton.setOnClickListener(v -> handleDeleteLastInputFromExpression());
        submitButton.setOnClickListener(v -> handleSubmit());
        setNumberButtonListeners();
        setOperationButtonListeners();
    }

    private void setNumberButtonListeners() {
        for (Button button : numberButtons) {
            setNumberClickListener(button);
        }
    }


    private void setNumberClickListener(Button button) {
        button.setOnClickListener(v -> {
            String currentExpression = currentStateOfExpression.getText().toString();
            if (currentExpression.length() == 0) {
                updateExpressionText(button.getText().toString());
            }
            if (currentExpression.length() > 0 && hasOperationBefore(currentExpression)) {
                updateExpressionText(button.getText().toString());
            }
            button.setEnabled(false);
        });
    }

    private boolean hasOperationBefore(String expression) {
        // Check if the expression ends with an operation symbol
        return expression.endsWith("+") ||
                expression.endsWith("-") ||
                expression.endsWith("*") ||
                expression.endsWith("/") ||
                expression.endsWith("(");
    }

    private void setOperationButtonListeners() {
        for (Button button : operationButtons) {
            setOperationClickListener(button);
        }
    }

    private void setOperationClickListener(Button button) {
        button.setOnClickListener(v -> updateExpressionText(button.getText().toString()));
    }

    private void initializeValues() {
        TextView resultsTextView1 = view.findViewById(R.id.resultFromUser1);
        TextView resultsTextView2 = view.findViewById(R.id.resultFromUser2);

        resultsTextView1.setText("0");
        resultsTextView2.setText("0");
        currentStateOfExpression.setText("");
    }

    private void handleStopButtonClick() {
        int count = clickCounter.getAndIncrement();
        if (count == 0) {
            handler.removeCallbacks(updateRunnableForFinalNumber);
            handler.post(updateRunnableForNumbers);
        } else if (count == 1) {
            handler.removeCallbacks(updateRunnableForNumbers);
            if (HomeScreenActivity.loggedUser.getId().equals(this.match.getPlayerTurn())) {
                JSONObject data = new JSONObject();
                try {
                    data.put("matchId", this.match.getId());
                    data.put("randomNumber", this.wantedNumberTextView.getText().toString());
                    JSONArray jsonArray = new JSONArray();
                    numberButtons.forEach(button -> {
                        jsonArray.put(button.getText().toString());
                    });
                    data.put("selectedNumbers", jsonArray);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SocketIOManager.getInstance().getSocket().emit("sendNumbers",data);
            }
            isUpdating = false;
        }

    }

    private Runnable updateRunnableForFinalNumber = new Runnable() {
        @Override
        public void run() {
            if (isUpdating) {
                int randomNumber = generateRandomNumber();
                wantedNumberTextView.setText(String.valueOf(randomNumber));
                handler.postDelayed(this, 30);
            }
        }
    };

    private Runnable updateRunnableForNumbers = new Runnable() {
        @Override
        public void run() {
            if (isUpdating) {
                int[] randomNumbersGenerated = generateRandomNumbersForExpression();
                setRandomNumberButtonValue(R.id.randomNr1, randomNumbersGenerated[0]);
                setRandomNumberButtonValue(R.id.randomNr2, randomNumbersGenerated[1]);
                setRandomNumberButtonValue(R.id.randomNr3, randomNumbersGenerated[2]);
                setRandomNumberButtonValue(R.id.randomNr4, randomNumbersGenerated[3]);
                setRandomNumberButtonValue(R.id.randomNr5, randomNumbersGenerated[4]);
                setRandomNumberButtonValue(R.id.randomNr6, randomNumbersGenerated[5]);
                handler.postDelayed(this, 30);
            }
        }
    };

    private int generateRandomNumber() {
        return random.nextInt(1000);
    }

    private int[] generateRandomNumbersForExpression() {
        int[] numbers = new int[6];
        for (int i = 0; i < 4; i++) {
            int randomNum = random.nextInt(9) + 1;
            numbers[i] = randomNum;
        }
        int fifthNum = random.nextInt(3);
        int[] fifthOptions = {10, 15, 20};
        numbers[4] = fifthOptions[fifthNum];
        int sixthNum = random.nextInt(4);
        int[] sixthOptions = {25, 50, 75, 100};
        numbers[5] = sixthOptions[sixthNum];

        return numbers;
    }

    private void setRandomNumberButtonValue(int buttonId, int value) {
        Button button = view.findViewById(buttonId);
        button.setText(String.valueOf(value));
    }

    private void updateExpressionText(String text) {
        currentStateOfExpression.append(text);
    }

    private void handleDeleteLastInputFromExpression() {
        String currentExpression = currentStateOfExpression.getText().toString();

        if (currentExpression.isEmpty()) {
            return;
        }
        if (isLastInputNumber(currentExpression)) {
            int lastNumberStart = findLastNumberStart(currentExpression);
            String updatedExpression = currentExpression.substring(0, lastNumberStart);
            currentStateOfExpression.setText(updatedExpression);
            enableNumberButton(currentExpression.substring(lastNumberStart, currentExpression.length()));
        } else {
            currentStateOfExpression.setText(currentExpression.substring(0, currentExpression.length() - 1));
        }
    }

    private boolean isLastInputNumber(String expression) {
        char lastChar = expression.charAt(expression.length() - 1);
        return Character.isDigit(lastChar);
    }

    private void enableNumberButton(String number) {
        numberButtons.forEach(button -> {
            if (button.getText().toString().equals(number)) {
                button.setEnabled(true);
            }
        });
    }
    private int findLastNumberStart(String expression) {
        int i = expression.length() - 1;
        while (i >= 0 && Character.isDigit(expression.charAt(i))) {
            i--;
        }
        return i + 1;
    }

    public void handleSubmit() {
        String expression = currentStateOfExpression.getText().toString();
        TextView finalResult = view.findViewById(R.id.resultFromUser1);
        JexlEngine jexlEngine = new JexlBuilder().create();
        JexlContext jexlContext = new MapContext();
        Object result = null;
        try {
            JexlExpression jexlExpression = jexlEngine.createExpression(expression);
            result = jexlExpression.evaluate(jexlContext);
            finalResult.setText(String.valueOf(result));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("matchId", this.match.getId());
            jsonObject.put("playerId", HomeScreenActivity.loggedUser.getId());
            jsonObject.put("result", result);
            jsonObject.put("wantedNumber", wantedNumberTextView.getText().toString());
            jsonObject.put("hasMoreRounds", this.match.getActiveGame().getCurrentRound() < this.match.getActiveGame().getRounds());
            SocketIOManager.getInstance().getSocket().emit("mojBrojResult", jsonObject);
        } catch (Exception e) {
            finalResult.setText("Invalid");
        }
    }

    private void gameResultDone() {
        SocketIOManager.getInstance().getSocket().on("gameFinished", args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            try {
                String player1Result = jsonObject.getString("player1Result");
                String player2Result = jsonObject.getString("player2Result");
                resultFromUser1.setText(player1Result);
                resultFromUser2.setText(player2Result);
                JSONObject jsonObject1 = (JSONObject) jsonObject.get("calculatedPoints");
                String points = jsonObject1.getString("points");
                String pointsWinnerID = jsonObject1.getString("player");
                String turn = jsonObject.getString("turn");
                int player1Points = jsonObject.getInt("player1Points");
                int player2Points = jsonObject.getInt("player2Points");
                this.match.setPlayerTurn(turn);
                this.match.getPlayer1().setPoints(player1Points);
                this.match.getPlayer2().setPoints(player2Points);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    try {
                        submitCallback.onSubmission(0);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, 1500);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private int calculatePoints(Integer passedResult) {
        if (String.valueOf(passedResult).equals(wantedNumberTextView.toString())) {
            return 20;
        }
        if (passedResult == null || passedResult == 0) {
            return 0;
        }
        return 5;
    }


    private void freezeScreen() {
        // Disable interactive UI elements
        stopButton.setEnabled(false);
        deleteButton.setEnabled(false);
        submitButton.setEnabled(false);
        // Disable number and operation buttons
        for (Button button : numberButtons) {
            button.setEnabled(false);
        }
        for (Button button : operationButtons) {
            button.setEnabled(false);
        }
        // Make the freezeOverlayView visible
        view.setVisibility(View.VISIBLE);
    }

    private void unfreezeScreen() {
        // Enable interactive UI elements
        stopButton.setEnabled(true);
        deleteButton.setEnabled(true);
        submitButton.setEnabled(true);
        // Enable number and operation buttons
        for (Button button : numberButtons) {
            button.setEnabled(true);
        }
        for (Button button : operationButtons) {
            button.setEnabled(true);
        }
        // Hide the freezeOverlayView
        view.setVisibility(View.VISIBLE);
    }
    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
