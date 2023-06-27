package com.example.pazzly.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private SubmitCallback submitCallback;
    public static FragmentKorakPoKorak newInstance() {
        return new FragmentKorakPoKorak();
    }

    public interface SubmitCallback {
        void onSubmission(int points);
    }

    public void setSubmitCallback(SubmitCallback callback) {
        submitCallback = callback;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_korak_po_korak, container, false);
        initializeStepsTextView();
        initializeView();
        firestoreDB = FirebaseFirestore.getInstance();
        handler = new Handler();
        initializeStepsListWithStringValues();
        return view;
    }

    private void initializeView() {
        submitButton = view.findViewById(R.id.submitKorakPoKorakAnswer);
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
        firestoreDB.collection("korakpokorak")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the documents
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                List<Object> array = (List<Object>) document.get("koraci");
                                konacniOdgovor = document.getString("finalno");
                                array.forEach(el -> {
                                    valueSteps.add(el.toString());
                                });
                            }
                        }
                        startUpdatingSteps();
                        submitButton.setOnClickListener(click -> handleSubmit());
                    } else {
                        // Handle unsuccessful retrieval
                    }
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

    public void handleSubmit() {
        TextInputLayout textInputLayout = view.findViewById(R.id.korakPoKorakAnswer);
        EditText editText = textInputLayout.getEditText();
        String passedAnswer = editText.getText().toString();
        if (!passedAnswer.equals(konacniOdgovor)) {
            return;
        }
        int points = 0;
        switch (currentStep) {
            case 0:
                points = 20;
                break;
            case 1:
                points = 18;
                break;
            case 2:
                points = 16;
                break;
            case 3:
                points = 14;
                break;
            case 4:
                points = 12;
                break;
            case 5:
                points = 10;
                break;
            case 6:
                points = 8;
                break;
            case 7:
                points = 6;
                break;
            default:
                points = 0;
                break;
        }
        if (submitCallback != null) {
            submitCallback.onSubmission(points);
        }
    }
}
