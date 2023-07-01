package com.example.pazzly.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pazzly.R;

public class ProfileActivity extends Activity {
    private EditText editTextTextPersonName;
    private EditText editTextTextEmailAddress;
    private ImageView image;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.profile);
        initializeViews();
        initialiseValues();
        image.setOnClickListener(click -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

    }

    private void initializeViews() {
        this.editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        this.editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        this.image = findViewById(R.id.imageView4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Log.d("SLIKAAA", "USPESNO UZETA SLIKAA: ");
        }
    }


    private void initialiseValues() {
        this.editTextTextPersonName.setText(HomeScreenActivity.loggedUser.getUsername());
        this.editTextTextEmailAddress.setText(HomeScreenActivity.loggedUser.getEmail());
    }
}
