
package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class MyLocation extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener { //
    Button Next;//

    ArrayList<String> myList;
    String radioValue, myEditText1Text, myEditText2Text;

    GoogleMap mMap;
    LocationRequest locationRequest;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    Boolean isPermissionGranted = false;

    FusedLocationProviderClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        Toast.makeText(MyLocation.this, "Location and internet must be turn on!", Toast.LENGTH_LONG).show();

//        myList = (ArrayList<String>) getIntent().getSerializableExtra("myList");
//        Bundle bundle = getIntent().getExtras();
//        radioValue = bundle.getString("radioValue");
//        myEditText1Text = bundle.getString("myEditText1Text");
//        myEditText2Text = bundle.getString("myEditText2Text");

        checkMyPermission();

        initMap();

        mLocationClient = new FusedLocationProviderClient(this);

//        getCurrLoc();

        Next = (Button) findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrLoc();
//                turnGPSOn();
//                getCurrLoc();
//                Toast.makeText(MyLocation.this, b.toString(), Toast.LENGTH_LONG).show();

//             Toast.makeText(MyLocation.this, myList.get(0) + " " + radioValue + " " + myEditText1Text + " " + myEditText2Text, Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(location.this, orderinformation.class);
//                startActivity(intent);
            }
        });



    }

    @SuppressLint("MissingPermission")
    private void getCurrLoc() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Location location = task.getResult();
                getoLocation(location.getLatitude(),location.getLongitude());
            }
        });
    }


    private void getoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
        mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.mMap.addMarker(new MarkerOptions().position(latLng).title("Tutorialspoint.com"));
        Toast.makeText(MyLocation.this, latLng.toString(), Toast.LENGTH_LONG).show();
    }

    private void initMap() {
        if (isPermissionGranted) {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);
        }

    }

//    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

//        this.mMap.setMyLocationEnabled(true);
//        LatLng TutorialsPoint = new LatLng(21, 57);
//        this.mMap.addMarker(new
//                MarkerOptions().position(TutorialsPoint).title("Tutorialspoint.com"));
//        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
    }

    public void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(MyLocation.this, "permission granted", Toast.LENGTH_LONG).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


