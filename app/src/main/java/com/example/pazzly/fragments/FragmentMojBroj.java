package com.example.pazzly.fragments;

import android.os.Bundle;
import android.os.Handler;
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

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static FragmentMojBroj newInstance() {
        return new FragmentMojBroj();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moj_broj, container, false);
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
        return view;
    }

    private void initializeViews() {
        wantedNumberTextView = view.findViewById(R.id.wantedNumber);
        stopButton = view.findViewById(R.id.stopMojBroj);
        deleteButton = view.findViewById(R.id.deleteExpressionButton);
        submitButton = view.findViewById(R.id.btnSubmitMojBroj);
        currentStateOfExpression = view.findViewById(R.id.currentStateOfExpression);
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
        try {
            JexlExpression jexlExpression = jexlEngine.createExpression(expression);
            Object result = jexlExpression.evaluate(jexlContext);
            finalResult.setText(String.valueOf(result));
        } catch (Exception e) {
            finalResult.setText("Invalid");
        }
    }
}
