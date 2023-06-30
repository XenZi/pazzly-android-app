package com.example.pazzly.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class FragmentSpojnice extends Fragment {
    private View view;
    private TextView question;
    private Button a1Button, a2Button, a3Button, a4Button,a5Button;
    private Button b1Button, b2Button, b3Button, b4Button,b5Button;

    private List<Button> btnColAList=new ArrayList<>();
    private List<Button> btnColBList=new ArrayList<>();

    private FragmentSpojnice.SubmitCallbackSpojnice callbackSpojnice;
    public interface SubmitCallbackSpojnice {
        void onSubmissionSpojnice(int points);
    }

    public void setSubmitCallbackSpojnice(FragmentSpojnice.SubmitCallbackSpojnice callback) {
        callbackSpojnice = callback;
    }

    private int numOfTries=0;

    public static FragmentSpojnice newInstance() {
        return new FragmentSpojnice();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_spojnice, container, false);

        initializeButtons();
        readFromDatabase();






        return view;
    }

    public void initializeButtons(){
        a1Button=view.findViewById(R.id.a1);
        a2Button=view.findViewById(R.id.a2);
        a3Button=view.findViewById(R.id.a3);
        a4Button=view.findViewById(R.id.a4);
        a5Button=view.findViewById(R.id.a5);

        btnColAList= Arrays.asList(a1Button,a2Button,a3Button,a4Button,a5Button);

        b1Button=view.findViewById(R.id.b1);
        b2Button=view.findViewById(R.id.b2);
        b3Button=view.findViewById(R.id.b3);
        b4Button=view.findViewById(R.id.b4);
        b5Button=view.findViewById(R.id.b5);

        btnColBList= Arrays.asList(b1Button,b2Button,b3Button,b4Button,b5Button);




        question=view.findViewById(R.id.question);
    }

//    public void readFromDatabase(){
//
//        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
//        firestoreDB.collection("spojnice").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()){
//                QuerySnapshot querySnapshot=task.getResult();
//                if (querySnapshot!=null){
//                    for(QueryDocumentSnapshot document:querySnapshot){
//
//                        String documentId= document.getId();
//                        String questionBase=document.getString("Pitanje");
//                        Log.d("PITANJE", questionBase);
//
//                        List<Object>aArray=(List<Object>) document.get("KolonaA");
//                        List<Object>bArray=(List<Object>) document.get("KolonaB");
//
//                        initialValues(aArray,bArray,questionBase);
//
//                        gameLogic(aArray,bArray);
//
//
//
//
//
//
//
//                    }
//                }
//
//            }
//            else
//            {
//
//            };
//
//        });
//
//    };

    public void readFromDatabase() {
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("spojnice").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    int randomIndex = (int) (Math.random() * documents.size());
                    QueryDocumentSnapshot randomDocument = (QueryDocumentSnapshot) documents.get(randomIndex);

                    String documentId = randomDocument.getId();
                    String questionBase = randomDocument.getString("Pitanje");
                    Log.d("PITANJE", questionBase);

                    List<Object> aArray = (List<Object>) randomDocument.get("KolonaA");
                    List<Object> bArray = (List<Object>) randomDocument.get("KolonaB");

                    initialValues(aArray, bArray, questionBase);
                    gameLogic(aArray, bArray);
                }
            } else {
                // Handle error
            }
        });
    }


    public void initialValues(List<Object> aArray,List<Object> bArray,String questionBase){

        ArrayList<Object> aArrayCopy = new ArrayList<>(aArray);
        Collections.shuffle(aArrayCopy);


        ArrayList<Object> bArrayCopy = new ArrayList<>(bArray);
        Collections.shuffle(bArrayCopy);


        a1Button.setText(aArrayCopy.get(0).toString());
        a2Button.setText(aArrayCopy.get(1).toString());
        a3Button.setText(aArrayCopy.get(2).toString());
        a4Button.setText(aArrayCopy.get(3).toString());
        a5Button.setText(aArrayCopy.get(4).toString());

        b1Button.setText(bArrayCopy.get(0).toString());
        b2Button.setText(bArrayCopy.get(1).toString());
        b3Button.setText(bArrayCopy.get(2).toString());
        b4Button.setText(bArrayCopy.get(3).toString());
        b5Button.setText(bArrayCopy.get(4).toString());

        question.setText(questionBase);
        //Nadji index u arreyu u bazi jednog kliknutog, nadji drugog, uporedi jesu li isti index





    }

    public void gameLogic(List<Object>aArray,List <Object>bArray){

        for (Button aButton : btnColAList) {

                aButton.setOnClickListener(view -> {





                    Button clickedAButton = (Button) view;
                    // Store the index of the clicked button from btnColAList
                    int aIndex = aArray.indexOf(clickedAButton.getText().toString());


                    Log.d("INDEXA", String.valueOf(aIndex));

                    // Set OnClickListener on btnColBList buttons
                    for (Button bButton : btnColBList) {
                        bButton.setOnClickListener(view1 -> {


                            // Handle click on a button from btnColBList
                            Button clickedBButton = (Button) view1;
                            // Store the index of the clicked button from btnColBList
                            int bIndex = bArray.indexOf(clickedBButton.getText().toString());
                            Log.d("INDEXB", String.valueOf(bIndex));

                            // Perform the check
                            if (aIndex == bIndex && aButton.isEnabled()) {
                                aButton.setEnabled(false);
                                bButton.setEnabled(false);
                                aButton.setBackgroundColor(Color.GREEN);
                                bButton.setBackgroundColor(Color.GREEN);
                                callbackSpojnice.onSubmissionSpojnice(2);
                                numOfTries+=1;




                                Log.d("Pair Match", "Indices match!");
                            } if(aIndex!=bIndex && aButton.isEnabled()) {
                                aButton.setEnabled(false);
                                aButton.setBackgroundColor(Color.RED);
                                numOfTries+=1;
                                Log.d("Pair Mismatch", "Indices do not match!");
                            }

                            if(numOfTries==5){
                                callbackSpojnice.onSubmissionSpojnice(0);
                            }
                        });
                    }
                });

        }


    }


}
