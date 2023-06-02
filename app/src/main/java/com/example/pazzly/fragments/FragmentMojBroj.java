package com.example.pazzly.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentMojBroj extends Fragment {
    private View view;
    private TextView wantedNumberTextView;
    private Button stopButton;
    private Handler handler;
    private Random random;
    private boolean isUpdating;
    private AtomicInteger clickCounter;

    public static FragmentMojBroj newInstance() {
        return new FragmentMojBroj();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moj_broj, container, false);
        initializeViews();
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
    }

    private void initializeListeners() {
        stopButton.setOnClickListener(v -> handleStopButtonClick());
    }

    private void initializeValues() {
        TextView resultsTextView1 = view.findViewById(R.id.resultFromUser1);
        TextView resultsTextView2 = view.findViewById(R.id.resultFromUser2);
        TextView expressionTextView = view.findViewById(R.id.currentStateOfExpression);
        Button randomNumberButton1 = view.findViewById(R.id.randomNr1);
        Button randomNumberButton2 = view.findViewById(R.id.randomNr2);
        Button randomNumberButton3 = view.findViewById(R.id.randomNr3);
        Button randomNumberButton4 = view.findViewById(R.id.randomNr4);
        Button randomNumberButton5 = view.findViewById(R.id.randomNr5);
        Button randomNumberButton6 = view.findViewById(R.id.randomNr6);

        resultsTextView1.setText("0");
        resultsTextView2.setText("0");
        expressionTextView.setText("");
        randomNumberButton1.setText("0");
        randomNumberButton2.setText("0");
        randomNumberButton3.setText("0");
        randomNumberButton4.setText("0");
        randomNumberButton5.setText("0");
        randomNumberButton6.setText("0");
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
}
