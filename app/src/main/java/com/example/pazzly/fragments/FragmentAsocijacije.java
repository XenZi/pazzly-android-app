package com.example.pazzly.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FragmentAsocijacije extends Fragment {


    private View view;



    private Button a1Button, a2Button, a3Button, a4Button;
    private Button b1Button, b2Button, b3Button, b4Button;
    private Button c1Button, c2Button, c3Button, c4Button;
    private Button d1Button, d2Button, d3Button, d4Button;
    private EditText aEditText, bEditText, cEditText, dEditText, fEditText;

    private Button submitButton;

    private ArrayList<Button> btnColAList=new ArrayList<>();
    private ArrayList<Button> btnColBList=new ArrayList<>();
    private ArrayList<Button> btnColCList=new ArrayList<>();
    private ArrayList<Button> btnColDList=new ArrayList<>();
    public static FragmentAsocijacije newInstance() {
        return new FragmentAsocijacije();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asocijacije, container, false);
        Log.d(TAG, "Views");
        a1Button = view.findViewById(R.id.A1);
        a2Button = view.findViewById(R.id.A2);
        a3Button = view.findViewById(R.id.A3);
        a4Button = view.findViewById(R.id.A4);

        b1Button = view.findViewById(R.id.B1);
        b2Button = view.findViewById(R.id.B2);
        b3Button = view.findViewById(R.id.B3);
        b4Button = view.findViewById(R.id.B4);

        c1Button = view.findViewById(R.id.C1);
        c2Button = view.findViewById(R.id.C2);
        c3Button = view.findViewById(R.id.C3);
        c4Button = view.findViewById(R.id.C4);

        d1Button = view.findViewById(R.id.D1);
        d2Button = view.findViewById(R.id.D2);
        d3Button = view.findViewById(R.id.D3);
        d4Button = view.findViewById(R.id.D4);



        aEditText = view.findViewById(R.id.A);
        bEditText = view.findViewById(R.id.B);
        cEditText = view.findViewById(R.id.C);
        dEditText = view.findViewById(R.id.D);
        fEditText = view.findViewById(R.id.F);

        aEditText.setEnabled(false);
        bEditText.setEnabled(false);
        cEditText.setEnabled(false);
        dEditText.setEnabled(false);
        fEditText.setEnabled(false);
        submitButton=view.findViewById(R.id.btnSubmit);

        readFromDatabase();











        return view;
    }

    public void readFromDatabase(){

        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        firestoreDB.collection("asocijacije").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot=task.getResult();
                if (querySnapshot!=null){
                    for(QueryDocumentSnapshot document:querySnapshot){

                        String documentId= document.getId();
                        String konacno=document.getString("Konacno");
                        List<Object>aArray=(List<Object>) document.get("GrupaA");
                        List<Object>bArray=(List<Object>) document.get("GrupaB");
                        List<Object>cArray=(List<Object>) document.get("GrupaC");
                        List<Object>dArray=(List<Object>) document.get("GrupaD");

                        buttonsGameLogic(aArray, bArray, cArray, dArray, konacno);
                        finalColumnsLogic(aArray, bArray, cArray, dArray, konacno);


                        aArray.forEach(el->{
                            Log.d("ARRELEMENT",el.toString());
                        });



                    }
                }

            }
            else
            {

            };

        });

    };

    public void buttonsNameMapping(List<Object> aArray, List<Object> bArray, List<Object> cArray, List<Object> dArray, String konacno){

        a1Button.setText(aArray.get(0).toString());
        a2Button.setText(aArray.get(1).toString());
        a3Button.setText(aArray.get(2).toString());
        a4Button.setText(aArray.get(3).toString());
        aEditText.setText(aArray.get(4).toString());


        b1Button.setText(bArray.get(0).toString());
        b2Button.setText(bArray.get(1).toString());
        b3Button.setText(bArray.get(2).toString());
        b4Button.setText(bArray.get(3).toString());
        bEditText.setText(bArray.get(4).toString());


        c1Button.setText(cArray.get(0).toString());
        c2Button.setText(cArray.get(1).toString());
        c3Button.setText(cArray.get(2).toString());
        c4Button.setText(cArray.get(3).toString());
        cEditText.setText(cArray.get(4).toString());

        d1Button.setText(dArray.get(0).toString());
        d2Button.setText(dArray.get(1).toString());
        d3Button.setText(dArray.get(2).toString());
        d4Button.setText(dArray.get(3).toString());
        dEditText.setText(dArray.get(4).toString());

        fEditText.setText(konacno.toString());

    }

    public void buttonListsInitializer(){


        btnColAList.add(a1Button);
        btnColAList.add(a2Button);
        btnColAList.add(a3Button);
        btnColAList.add(a4Button);

        btnColBList.add(b1Button);
        btnColBList.add(b2Button);
        btnColBList.add(b3Button);
        btnColBList.add(b4Button);

        btnColCList.add(c1Button);
        btnColCList.add(c2Button);
        btnColCList.add(c3Button);
        btnColCList.add(c4Button);

        btnColDList.add(d1Button);
        btnColDList.add(d2Button);
        btnColDList.add(d3Button);
        btnColDList.add(d4Button);

    }

    public void buttonsGameLogic(List<Object> aArray, List<Object> bArray, List<Object> cArray, List<Object> dArray, String konacno){
        buttonListsInitializer();


        for (int i = 0; i < btnColAList.size(); i++) {
            final int[] o = {0};


            Button button = btnColAList.get(i);
            final int index = i;

            // Create a final copy of 'i'

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String buttonText = aArray.get(index).toString();
                    button.setText(buttonText);
                    o[0]++;
                    if (o[0] != 0){
                        Log.d("USLOJE", String.valueOf(o[0]));
                        aEditText.setEnabled(true);
                        fEditText.setEnabled(true);
                    }
                    Log.d("OpenColA", String.valueOf(o[0]));

                }
            });
        }

        for (int i = 0; i < btnColBList.size(); i++) {
            final int[] o = {0};
            Button button = btnColBList.get(i);
            final int index = i; // Create a final copy of 'i'

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Button click action
                    String buttonText = bArray.get(index).toString();
                    button.setText(buttonText);
                    o[0]++;
                    if (o[0] != 0){
                        Log.d("USLOJE", String.valueOf(o[0]));
                        bEditText.setEnabled(true);
                        fEditText.setEnabled(true);
                    }
                }
            });
        }

        for (int i = 0; i < btnColCList.size(); i++) {
            final int[] o = {0};
            Button button = btnColCList.get(i);
            final int index = i; // Create a final copy of 'i'

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Button click action
                    String buttonText = cArray.get(index).toString();
                    button.setText(buttonText);
                    o[0]++;
                    if (o[0] != 0){
                        Log.d("USLOJE", String.valueOf(o[0]));
                        cEditText.setEnabled(true);
                        fEditText.setEnabled(true);
                    }
                }
            });
        }

        for (int i = 0; i < btnColDList.size(); i++) {
            final int[] o = {0};
            Button button = btnColDList.get(i);
            final int index = i; // Create a final copy of 'i'

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Button click action
                    String buttonText = dArray.get(index).toString();
                    button.setText(buttonText);

                    o[0]++;
                    if (o[0] != 0){
                        Log.d("USLOJE", String.valueOf(o[0]));
                        dEditText.setEnabled(true);
                        fEditText.setEnabled(true);
                    }
                }
            });
        }



    }

    public void finalColumnsLogic(List<Object> aArray, List<Object> bArray, List<Object> cArray, List<Object> dArray, String konacno){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredValueA = aEditText.getText().toString();
                String enteredValueB = bEditText.getText().toString();
                String enteredValueC = cEditText.getText().toString();
                String enteredValueD = dEditText.getText().toString();
                String enteredValueF = fEditText.getText().toString();

                boolean anyEditTextChanged = false;

                if (aArray.size() > 4 && enteredValueA.equals(aArray.get(4).toString())) {
                    aEditText.setText(aArray.get(4).toString());
                    aEditText.setEnabled(false);
                    anyEditTextChanged = true;
                }

                if (bArray.size() > 4 && enteredValueB.equals(bArray.get(4).toString())) {
                    bEditText.setText(bArray.get(4).toString());
                    bEditText.setEnabled(false);
                    anyEditTextChanged = true;
                }

                if (cArray.size() > 4 && enteredValueC.equals(cArray.get(4).toString())) {
                    cEditText.setText(cArray.get(4).toString());
                    cEditText.setEnabled(false);
                    anyEditTextChanged = true;
                }

                if (dArray.size() > 4 && enteredValueD.equals(dArray.get(4).toString())) {
                    dEditText.setText(dArray.get(4).toString());
                    dEditText.setEnabled(false);
                    anyEditTextChanged = true;
                }

                if (enteredValueF.equals(konacno)) {
                    fEditText.setText(konacno);
                    fEditText.setEnabled(false);
                    anyEditTextChanged = true;
                }

                if (!anyEditTextChanged) {
                    // Handle case when no EditText field was changed
                }
            }







        });



    };
}
