package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button back;
    TextView userE, userPh, userPass, userUs;

    SharedPreferences sharedpreferences;
    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userE = findViewById(R.id.userEmail);
        userPh = findViewById(R.id.userPhoneNumber);
        userUs = findViewById(R.id.userUserName);
        userPass = findViewById(R.id.userPassword);


        userE.setText("getting data now !!!");
        userPh.setText("getting data now !!!");
        userUs.setText("getting data now !!!");
        userPass.setText("getting data now !!!");

        email = getEmailFromCache ();
        getProfileDate(email);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    String getEmailFromCache (){
        sharedpreferences = getSharedPreferences("new", 0);
        return sharedpreferences.getString("logged_in", "");
    }

    void getProfileDate (String email){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshot.child("Users").child(email).child("Em").getValue();
                userE.setText(String.valueOf(dataSnapshot.child("Users").child(email).child("Em").getValue()));
                userPh.setText(String.valueOf(dataSnapshot.child("Users").child(email).child("Ph").getValue()));
                userUs.setText(String.valueOf(dataSnapshot.child("Users").child(email).child("user").getValue()));
                userPass.setText(String.valueOf(dataSnapshot.child("Users").child(email).child("Pass").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}