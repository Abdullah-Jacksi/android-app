package com.example.myapplication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class welcomescreen extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomescreen);


        Thread thread = new Thread () {
        @Override
        public void run() {
        try {
        sleep(1000);

            saveToCache();

        } catch (InterruptedException e) {
        e.printStackTrace();
         }
         }
        };
        thread.start();
    }
    void saveToCache() {
        sharedpreferences = getSharedPreferences("new", 0);
        editor = sharedpreferences.edit();
//        editor.remove("logged_in");
        if (!sharedpreferences.contains("logged_in")) {
//            Toast.makeText(OrderInformation.this, "not exist orderNumber", Toast.LENGTH_SHORT).show();
//            editor.putInt("logged_in", 0);
//            editor.commit();
            Intent intent = new Intent(getApplicationContext() , introscreen1.class); // introscreen1.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext() , items.class); // introscreen1.class);
            startActivity(intent);
            finish();
//            Toast.makeText(OrderInformation.this, "yes exist orderNumber", Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(OrderInformation.this, String.valueOf(orderNumber), Toast.LENGTH_SHORT).show();
    }
//    public void checkMyPermission() {
//        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                Toast.makeText(welcomescreen.this, "permission granted", Toast.LENGTH_LONG).show();
////                isPermissionGranted = true;
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), "");
//                intent.setData(uri);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                permissionToken.continuePermissionRequest();
//            }
//        }).check();
//    }
}