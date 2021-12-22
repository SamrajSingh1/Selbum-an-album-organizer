package com.gallery.selbum.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gallery.selbum.R;
import com.gallery.selbum.Session.SharedPrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 8778;
    Button b;
    int a=1;

    public  String spath="";
    public String filename1[]=new String[100];
    int x=0;//to act as pointer to file name array.
    int y=0;
    int len=0;//to check length of directory.
    int z=0;// to store the integer representing how many responses from server we got.

    public void selectFolder(View v) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fpath = data.getData().getPath();
        spath=fpath.replace("external_files","sdcard") ;
        int n=spath.length();
        for(int i=n-1;i>=0;i--){
            if(spath.charAt(i)=='/'){
                spath=spath.substring(0,i+1);
                break;
            }
        }

        Toast.makeText(this,"Image path is " + spath,Toast.LENGTH_LONG).show();

        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        findViewById(R.id.btnLogout).setOnClickListener(v->{
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        });

        b=findViewById(R.id.button);

        final RequestQueue requestQ= Volley.newRequestQueue(this);
        final String[] s = new String[2];

        final Context s1=getApplicationContext();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spath=spath.replace("primary:", "");
                spath=spath.replace("document/","");


                spath="/storage/emulated/0"+spath;
                if(spath.equals(""))
                    Toast.makeText(s1,"Please Select path",Toast.LENGTH_LONG).show();
                else{
                    z=0;
                    len=0;
                    Toast.makeText(s1,"Please WAIT while your app is organizing images ",Toast.LENGTH_LONG).show();
                    File familyFolder = new File(spath+"/FamilyPhotos");
                    if(!familyFolder.exists())
                        familyFolder.mkdir();
                    File DocumentFolder = new File(spath+"/DocumentImages");
                    if(!DocumentFolder.exists())
                        DocumentFolder.mkdir();
                    File NatureImagesFolder = new File( spath+"/NatureImages");
                    if(!NatureImagesFolder.exists())
                        NatureImagesFolder.mkdir();


                    Log.d("ss", spath);

                    final File dir = new File(spath);




                    final String[] EXTENSIONS = new String[]{"jpg", "png"};
                    //filter to identify images based on their extensions
                    final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

                        @Override
                        public boolean accept(final File dir, final String name) {
                            for (final String ext : EXTENSIONS) {
                                if (name.endsWith("." + ext)) {
                                    s[1]="."+ext;
                                    return (true);
                                }
                            }
                            return (false);
                        }
                    };
                    len=(int)dir.length();
                    Log.d("aa", dir.toString());



                    if (dir.isDirectory()) { // make sure it's a directory


                        try {
                            for (final File imagefile : dir.listFiles(IMAGE_FILTER)) {
                                FileInputStream fis = null;
                                try {
                                    fis = new FileInputStream(imagefile);
                                    Log.d("s3333", "sangam");

                                } catch (FileNotFoundException e) {

                                }
                                Bitmap bm = BitmapFactory.decodeStream(fis);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                final String encImage = Base64.encodeToString(b, Base64.DEFAULT);
                                String url = "http://13.235.51.183:5111/sp";
                                filename1[x] = imagefile.getName();
                                x++;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        z++;

                                        Toast.makeText(s1, "Image " + response.substring(1) + " Accessed.", Toast.LENGTH_LONG).show();
                                        if (response.substring(0, 1).equals("0")) {
                                            File j = new File(spath + "/FamilyPhotos/" + filename1[y]);
                                            boolean t = imagefile.renameTo(j);
                                            y++;

                                        }
                                        if (response.substring(0, 1).equals("1")) {
                                            File j = new File(spath + "/DocumentImages/" + filename1[y]);
                                            boolean t = imagefile.renameTo(j);
                                            y++;
                                        }
                                        if (response.substring(0, 1).equals("2")) {
                                            File j = new File(spath + "/NatureImages/" + filename1[y]);
                                            boolean t = imagefile.renameTo(j);
                                            y++;
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        z++;
                                        //Toast.makeText(s1,"Please wait till app organizing your images",Toast.LENGTH_LONG).show();
                                    }
                                }) {

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("image", encImage); //encImage is base64 String of selected image
                                        hashMap.put("ext", s[1]);
                                        hashMap.put("fname", filename1[y]);
                                        return hashMap;
                                    }
                                };

                                requestQ.add(stringRequest);


                            }
                        }
                        catch (Exception e){
                            Log.d("eee", dir.listFiles().toString());
                        }


                    }
                    else
                        Toast.makeText(s1,"Directory path is not appropriate ",Toast.LENGTH_LONG).show();

                }


            }

        });

    }
}