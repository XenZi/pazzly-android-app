package com.example.pazzly.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pazzly.R;

public class FragmentAsocijacije extends Fragment {
    private View view;

    private Button a1Button, a2Button, a3Button, a4Button;
    private Button b1Button, b2Button, b3Button, b4Button;
    private Button c1Button, c2Button, c3Button, c4Button;
    private Button d1Button, d2Button, d3Button, d4Button;
    private EditText aEditText, bEditText, cEditText, dEditText, fEditText;
    public static FragmentAsocijacije newInstance() {
        return new FragmentAsocijacije();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asocijacije, container, false);

        View view = inflater.inflate(R.layout.fragment_asocijacije, container, false);


        // Initialize the buttons and EditText views
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


        // Repeat the same for other buttons

        aEditText = view.findViewById(R.id.A);
        bEditText = view.findViewById(R.id.B);
        cEditText = view.findViewById(R.id.C);
        dEditText = view.findViewById(R.id.D);
        fEditText = view.findViewById(R.id.F);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("your_collection_name/LEKTOR");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> lektorMap = (Map<String, Object>) dataSnapshot.getValue();

                    // Map values to buttons
                    a1Button.setText((String) lektorMap.get("A1"));
                    a2Button.setText((String) lektorMap.get("A2"));
                    a3Button.setText((String) lektorMap.get("A3"));
                    a4Button.setText((String) lektorMap.get("A4"));
                    // Repeat the same for other buttons

                    // Rest of the code...
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);


    }


}
