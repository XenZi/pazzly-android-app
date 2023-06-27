package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pazzly.MainActivity;
import com.example.pazzly.R;
import com.example.pazzly.domain.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.UUID;

public class RegisterScreenActivity extends Activity {
    private TextInputLayout emailRegisterInput;
    private TextInputLayout usernameRegisterInput;
    private TextInputLayout passwordRegisterInput;
    private TextInputLayout passwordRepeatRegisterInput;
    private Button registerPageRegisterBtn;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "Register Screen", Toast.LENGTH_SHORT).show();
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.register);
        initializeViewElements();
        registerPageRegisterBtn.setOnClickListener(v -> submitRegister());
    }

    private void initializeViewElements() {
        this.emailRegisterInput = findViewById(R.id.emailRegisterInput);
        this.usernameRegisterInput = findViewById(R.id.usernameRegisterInput);
        this.passwordRegisterInput = findViewById(R.id.passwordRegisterInput);
        this.passwordRepeatRegisterInput = findViewById(R.id.passwordRepeatRegisterInput);
        this.registerPageRegisterBtn = findViewById(R.id.registerPageRegisterBtn);
    }

    private void submitRegister() {
        String enteredEmail = this.emailRegisterInput.getEditText().getText().toString();
        String enteredUsername = this.usernameRegisterInput.getEditText().getText().toString();
        String enteredPassword = this.passwordRegisterInput.getEditText().getText().toString();
        String confirmedPassword = this.passwordRepeatRegisterInput.getEditText().getText().toString();

        if (!enteredPassword.equals(confirmedPassword)) {
            Toast.makeText(this, "Password and entered password are not the same", Toast.LENGTH_SHORT);
            return;
        }

        addDataToFirestore(enteredEmail, enteredUsername, enteredPassword);
    }

    private void addDataToFirestore(String enteredEmail, String enteredUsername, String enteredPassword) {
        CollectionReference dbUsers = firestore.collection("users");
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(enteredEmail);
        user.setUsername(enteredUsername);
        user.setPassword(enteredPassword);
        user.setStars(0);
        user.setTokens(5);

        dbUsers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(RegisterScreenActivity.this, "Your have successfully registered", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(RegisterScreenActivity.this, "Fail to register \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
