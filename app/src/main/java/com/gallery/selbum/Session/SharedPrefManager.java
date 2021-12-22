package com.gallery.selbum.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREFF_NAME = "mysharedpref12";
    private static final String KEY_PMO_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MOBILE_NO = "mobileno";
    private static final String KEY_ADDRESS = "address";
    private static final String REG_ID="reg_id";
    private static final String KEY_PROJECT_ID = "PID";
    private static final String KEY_PROJECT_NAME = "PNAME";
    private static final String KEY_ACTIVITY_NAME = "activityName";
    private static final String KEY_ACTIVITY_ID = "activityId";
    private static final String KEY_SANSTIONAMNT = "amnt";
    private static final String KEY_VILLAGE = "village";
    private static final String KEY_EMP_ID = "empid";
    private static final String KEY_DEGIGNATION = "Desg";
    private static final String KEY_TOTAL_SALLERY = "ts";
    private static final String KEY_ONEDAY_SALLERY = "OneDatSallry";
    private static final String KEY_PFA = "pfa";
    private static final String KEY_ESICE = "esice";
    private static final String KEY_PHOTO = "photo";
    private static final String FIREBASE_ID = "firebase_id";

    private static final String KEY_USER_NAME = "username";
    private static final String KRY_LABOUR_ID = "LABOURiD";


    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean logout() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        return true;
    }


    public boolean saveUserLogin(String Name, String email,String firebase_id,String password) {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_NAME, Name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(FIREBASE_ID,firebase_id);
        editor.apply();
        return true;
    }
    public boolean saveRegId(String regId){
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(REG_ID, regId);
        editor.apply();
        return true;
    }

    public boolean saveLabourId(int lid) {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(KRY_LABOUR_ID, lid);


        editor.apply();
        return true;
    }

    public boolean saveProject(int ProjectId, String ProjectName, String SanstionAmount, String Village) {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(KEY_PROJECT_ID, ProjectId);
        editor.putString(KEY_PROJECT_NAME, ProjectName);

        editor.putString(KEY_SANSTIONAMNT, SanstionAmount);
        editor.putString(KEY_VILLAGE, Village);
        editor.apply();
        return true;
    }


    public int getLabourId() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getInt(KRY_LABOUR_ID, 0);
    }



    public  String getRegId() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(REG_ID,"");
    }

    public int getEmpId() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getInt(KEY_EMP_ID, 0);
    }

    public int getProjectId() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getInt(KEY_PROJECT_ID, 0);
    }

    public String getProjectName() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_PROJECT_NAME, null);
    }

    public String getSansAmount() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_SANSTIONAMNT, null);
    }


    public int getUserId() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getInt(KEY_PMO_ID, 0);
    }

    public String getUserName() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_NAME, "");
    }
    public String getProfilePic() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_PHOTO, "");
    }

    public String getEmail() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_EMAIL, "");
    }

    public String getMobileNo() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_MOBILE_NO, "");
    }

    public String getAddress() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_ADDRESS, "");
    }

    public String getPassword() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_PASSWORD, "");
    }

    public int getActivityId() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getInt(KEY_ACTIVITY_ID, 0);
    }

    public String getActivityName() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_ACTIVITY_NAME, "");
    }

    public String getVillageName() {
        SharedPreferences settings = mCtx.getSharedPreferences(SHARED_PREFF_NAME, Context.MODE_PRIVATE);
        return settings.getString(KEY_VILLAGE, "");
    }
}
