package com.gallery.selbum;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.gallery.selbum.Session.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Selbum  extends MultiDexApplication {

    public static Selbum instance;
    public static final String TAG = "Selbum";
    private static Context mContext = null;
    private static SharedPreferences mSharedPreferences;
    String notificationChannelID = "TestChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mSharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        instance = this;
        createNotificationChannel();
//  all strings >> load offline
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
// Get token
        // [START retrieve_current_token]
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        } else {
                            String token = task.getResult();
                            Log.e(TAG, "onComplete: "+token);
                            SharedPrefManager.getInstance(getAppContext()).saveRegId(token);
                        }
                    }
                });
        // [END retrieve_current_token]


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Selbum getInstance() {
        return instance;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static String getRestartIntent() {
        return "com.bss.posigraph.restart";
    }

    public static Context getAppContext() {
        return mContext;
    }
}
