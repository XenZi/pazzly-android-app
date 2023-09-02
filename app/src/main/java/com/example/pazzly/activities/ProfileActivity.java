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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pazzly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends Activity {
    private EditText editTextTextPersonName;
    private EditText editTextTextEmailAddress;
    private ImageView image;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.profile);
        initializeViews();
        storageReference = FirebaseStorage.getInstance().getReference();
        this.firestore = FirebaseFirestore.getInstance();
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
        Picasso.get().load(HomeScreenActivity.loggedUser.getProfileImg()).into(this.image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
            Log.d("SLIKAAA", "USPESNO UZETA SLIKAA: ");
        }
    }

    private void uploadImage(Uri imageUri) {
            // Define a reference to the location in Firebase Storage where you want to store the image.
            // You can specify a folder path and file name.
            StorageReference imageRef = storageReference.child("profile_images/" + HomeScreenActivity.loggedUser.getId() + ".jpg");

            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                // Now, you can get the download URL and use it as needed.
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    // Save this downloadUrl to your Firestore database or use it in your app as needed.
                    Query query = firestore.collection("users")
                            .whereEqualTo("id", HomeScreenActivity.loggedUser.getId());
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if (task.isSuccessful()) {
                              QuerySnapshot querySnapshot = task.getResult();
                              if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                  DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                  documentSnapshot.getReference().update("profileImg", downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {

                                      }
                                  });
                              }
                          }
                      }
                  });
                    Picasso.get().load(downloadUrl).into(this.image);

                    Log.d("UPLOAD_SUCCESS", "Image uploaded. Download URL: " + downloadUrl);
                });
            }).addOnFailureListener(e -> {
                // Handle the failure
                Log.e("UPLOAD_ERROR", "Image upload failed: " + e.getMessage());
            });

    }


    private void initialiseValues() {
        this.editTextTextPersonName.setText(HomeScreenActivity.loggedUser.getUsername());
        this.editTextTextEmailAddress.setText(HomeScreenActivity.loggedUser.getEmail());
    }


}
