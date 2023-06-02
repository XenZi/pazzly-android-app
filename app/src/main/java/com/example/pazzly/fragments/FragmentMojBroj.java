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

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentMojBroj extends Fragment {
    private TextView wantedNumberTextView;
    private Button stopButton;

    private Handler handler;

    private Random random;
    private boolean isUpdating;

    public static FragmentMojBroj newInstance() {
        return new FragmentMojBroj();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moj_broj, container, false);
        AtomicInteger i = new AtomicInteger();
        wantedNumberTextView = view.findViewById(R.id.wantedNumber);
        TextView resultsTextView1 = view.findViewById(R.id.resultFromUser1);
        TextView resultsTextView2 = view.findViewById(R.id.resultFromUser2);
        stopButton = view.findViewById(R.id.stopMojBroj);
        Button randomNumberButton1 = view.findViewById(R.id.randomNr1);
        Button randomNumberButton2 = view.findViewById(R.id.randomNr2);
        Button randomNumberButton3 = view.findViewById(R.id.randomNr3);
        Button randomNumberButton4 = view.findViewById(R.id.randomNr4);
        Button randomNumberButton5 = view.findViewById(R.id.randomNr5);
        Button randomNumberButton6 = view.findViewById(R.id.randomNr6);

        resultsTextView1.setText("0");
        resultsTextView2.setText("0");

        handler = new Handler();
        random = new Random();
        isUpdating = true;

        Runnable updateRunnableForFinalNumber = new Runnable() {
            @Override
            public void run() {
                if (isUpdating) {
                    int randomNumber = generateRandomNumber();
                    wantedNumberTextView.setText(String.valueOf(randomNumber));
                    handler.postDelayed(this, 30); // Change the interval as desired
                }
            }
        };

        Runnable updateRunnableForNumbers = new Runnable() {
            @Override
            public void run() {
                if (isUpdating) {
                    int[] randomNumbersGenerated = generateRandomNumbersForExpression();
                    randomNumberButton1.setText(String.valueOf(randomNumbersGenerated[0]));
                    randomNumberButton2.setText(String.valueOf(randomNumbersGenerated[1]));
                    randomNumberButton3.setText(String.valueOf(randomNumbersGenerated[2]));
                    randomNumberButton4.setText(String.valueOf(randomNumbersGenerated[3]));
                    randomNumberButton5.setText(String.valueOf(randomNumbersGenerated[4]));
                    randomNumberButton6.setText(String.valueOf(randomNumbersGenerated[5]));
                    handler.postDelayed(this, 30); // Change the interval as desired
                }
            }
        };


        handler.post(updateRunnableForFinalNumber);

        stopButton.setOnClickListener(v -> {
            if (i.get() == 0) {
                handler.removeCallbacks(updateRunnableForFinalNumber);
                handler.post(updateRunnableForNumbers);
                i.getAndIncrement();
            } else if (i.get() == 1) {
                handler.removeCallbacks(updateRunnableForNumbers);
                i.getAndIncrement();
                isUpdating = false;
            }
        });

        return view;
    }

    private int generateRandomNumber() {
        return random.nextInt(1000); // Change the upper bound as per your requirements
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

}
