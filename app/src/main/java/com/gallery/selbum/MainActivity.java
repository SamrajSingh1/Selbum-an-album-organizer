package com.gallery.selbum;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.gallery.selbum.Session.SharedPrefManager;
import com.gallery.selbum.ui.DashboardActivity;
import com.gallery.selbum.ui.ImageSliderActivity;
import com.gallery.selbum.ui.LoginActivity;
import com.google.firebase.FirebaseApp;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler(Looper.myLooper()).postDelayed(() -> {
            if (SharedPrefManager.getInstance(getApplicationContext()).getPassword().isEmpty()){
                startActivity(new Intent(getApplicationContext(), ImageSliderActivity.class));
            }else if (!SharedPrefManager.getInstance(getApplicationContext()).getPassword().isEmpty()){
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        },2000);
    }
}