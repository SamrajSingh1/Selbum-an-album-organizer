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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gallery.selbum.R;
import com.gallery.selbum.Session.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {
    TextView signUp;
    CircularProgressButton login;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userDatabaseReference;
    TextInputEditText email, password;
    private DatabaseReference getUserDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.et_email);
        signUp=findViewById(R.id.tvSignUp);
        signUp.setOnClickListener(v->{ startActivity(new Intent(getApplicationContext(),SignUpActivity.class));});
        password = findViewById(R.id.et_password);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(v -> {
            if (email.getText().toString().length() == 0) {
                email.setError("field can't be empty");
                email.requestFocus();
            } else if (password.getText().toString().length() == 0) {
                password.setError("field can't be empty");
                password.requestFocus();
            } else {
                functionFirebaseLogin(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void functionFirebaseLogin(String email, String password) {
        new Handler().postDelayed(() -> login.startAnimation(),3000);
        if (isConnected(LoginActivity.this)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userUID = mAuth.getCurrentUser().getUid();
                            String userDeiceToken = SharedPrefManager.getInstance(getApplicationContext()).getRegId();
                            userDatabaseReference.child(userUID).child("device_token").setValue(userDeiceToken)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            login.stopAnimation();
                                            login.setDrawableBackground(getResources().getDrawable(R.drawable.button_shape_no_fill));
                                            login.recoverInitialState();
                                            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
                                            getUserDatabaseReference.keepSynced(true); // for offline
                                            getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    // retrieve data from db
                                                    String user_status = dataSnapshot.child("user_status").getValue().toString();
                                                    String email = dataSnapshot.child("user_email").getValue().toString();
                                                    String username = dataSnapshot.child("user_name").getValue().toString();
                                                    SharedPrefManager.getInstance(getApplicationContext()).saveUserLogin(username,email,user_id,password);
                                                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent loginIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(loginIntent);
                                                    finish();
                                                 }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    });

                        } else {
                            login.stopAnimation();
                            login.setDrawableBackground(getResources().getDrawable(R.drawable.button_shape_no_fill));
                            login.recoverInitialState();
                            Toast.makeText(getApplicationContext(),"invalid username or password", Toast.LENGTH_SHORT).show();
                         }
                    });
        }

    }
}