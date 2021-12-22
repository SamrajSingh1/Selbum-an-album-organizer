package com.gallery.selbum.ui;

import static com.gallery.selbum.helper.Constants.isConnected;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gallery.selbum.R;
import com.gallery.selbum.Session.SharedPrefManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class SignUpActivity extends AppCompatActivity {
    CircularProgressButton signUp;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference storeDefaultDatabaseReference;
    TextInputEditText email, username, password, confirmPassword;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        signUp = findViewById(R.id.btnSignup);
        email = findViewById(R.id.et_email);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        signUp.setOnClickListener(v -> {

             if (username.getText().toString().length() == 0) {
                username.setError("field can't be empty");
                username.requestFocus();
            } else if (email.getText().toString().length() == 0) {
                email.setError("field can't be empty");
                email.requestFocus();
            } else if (password.getText().toString().length() == 0) {
                password.setError("field can't be empty");
                password.requestFocus();
            } else if (password.getText().toString().length()<6){
                 password.setError("please enter at least 6 digit password");
                 password.requestFocus();
             }else {
                functionRegisterUserWithFirebase(username.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        });

    }

    private void functionRegisterUserWithFirebase(String Username, String Email, String Password) {
        new Handler().postDelayed(() -> signUp.startAnimation(),3000);
        if (isConnected(SignUpActivity.this)) {
            //gooogle firebase login
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String deviceToken = SharedPrefManager.getInstance(getApplicationContext()).getRegId();

                            // get and link storage
                            String current_userID = mAuth.getCurrentUser().getUid();
                            storeDefaultDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(current_userID);
                            storeDefaultDatabaseReference.child("user_name").setValue(Username);
                            storeDefaultDatabaseReference.child("verified").setValue("true");
                            storeDefaultDatabaseReference.child("search_name").setValue(Username.trim().toLowerCase());
                            storeDefaultDatabaseReference.child("user_email").setValue(Email.trim().toLowerCase());
                            storeDefaultDatabaseReference.child("user_nickname").setValue(Username.trim().toLowerCase());
                            storeDefaultDatabaseReference.child("user_pass").setValue(Password.trim());
                            storeDefaultDatabaseReference.child("user_gender").setValue("");
                            storeDefaultDatabaseReference.child("user_profession").setValue("");
                            storeDefaultDatabaseReference.child("created_at").setValue(ServerValue.TIMESTAMP);
                            storeDefaultDatabaseReference.child("active_now").setValue(ServerValue.TIMESTAMP);
                            storeDefaultDatabaseReference.child("user_status").setValue("Hi, I'm new selbum user");
                            storeDefaultDatabaseReference.child("user_image").setValue("default_image"); // Original image
                            storeDefaultDatabaseReference.child("device_token").setValue(deviceToken);
                            storeDefaultDatabaseReference.child("user_thumb_image").setValue("default_image")
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // SENDING VERIFICATION EMAIL TO THE REGISTERED USER'S EMAIL
                                            user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_SHORT).show();
                                                signUp.stopAnimation();
                                                signUp.recoverInitialState();
                                                signUp.setDrawableBackground(getResources().getDrawable(R.drawable.button_shape_no_fill));
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            }

                                        } else {
                                            Log.d("fire2", task.getException().getMessage());
                                        }
                                    });

                        } else {
                            signUp.stopAnimation();
                            signUp.setDrawableBackground(getResources().getDrawable(R.drawable.button_shape_no_fill));
                            signUp.revertAnimation();
                            signUp.recoverInitialState();
                            Log.d("fire", task.getException().getMessage());
                            Toast.makeText(getApplicationContext(), task.getException().getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

}