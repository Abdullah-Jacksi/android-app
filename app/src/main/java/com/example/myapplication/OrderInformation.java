package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderInformation extends AppCompatActivity {


    ArrayList<String> myList, locationList;
    String radioValue, myEditText1Text, myEditText2Text;

    private Button Confirmation;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    int orderNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderinformation);


        myList = (ArrayList<String>) getIntent().getSerializableExtra("myList");
        locationList = (ArrayList<String>) getIntent().getSerializableExtra("locationList");
        Bundle bundle = getIntent().getExtras();
        radioValue = bundle.getString("radioValue");
        myEditText1Text = bundle.getString("myEditText1Text");
        myEditText2Text = bundle.getString("myEditText2Text");

        sharedpreferences = getSharedPreferences("new",0);
        editor = sharedpreferences.edit();
        if(!sharedpreferences.contains("orderNumber")){
            Toast.makeText(OrderInformation.this, "not exist orderNumber", Toast.LENGTH_LONG).show();
            editor.putInt("orderNumber" , 0);
            editor.commit();
        }else{
            Toast.makeText(OrderInformation.this, "yes exist orderNumber", Toast.LENGTH_LONG).show();
            orderNumber += sharedpreferences.getInt("orderNumber" , 0);
        }
        Toast.makeText(OrderInformation.this, String.valueOf(orderNumber), Toast.LENGTH_LONG).show();


        Confirmation = (Button) findViewById(R.id.Confirmation);
        Confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                    Toast.makeText(OrderInformation.this, myList.size() + " " + radioValue + " " + myEditText1Text + " " + myEditText2Text, Toast.LENGTH_LONG).show();
//                    Toast.makeText(OrderInformation.this, locationList.get(0) + " " + locationList.get(1) + " " + locationList.get(2) + " " + locationList.get(3), Toast.LENGTH_LONG).show();
//                orderNumber += 1;
//                editor.putInt("orderNumber" , orderNumber);
//                editor.commit();
//                Toast.makeText(OrderInformation.this, String.valueOf(orderNumber), Toast.LENGTH_LONG).show();
                myList.add("paper");
                myList.add("aluminum");
                radioValue = "Amount";
                myEditText1Text = "50";
                myEditText2Text = "another note";

                Confirmation.setEnabled(false);

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("orderNumber", orderNumber + 1);
                        userdataMap.put("myList", myList);
                        userdataMap.put("locationList", locationList);
                        userdataMap.put("radioValue", radioValue);
                        userdataMap.put("myEditText1Text", myEditText1Text);
                        userdataMap.put("myEditText2Text", myEditText2Text);
                        RootRef.child("Orders").child(String.valueOf((orderNumber + 1))).updateChildren(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            orderNumber += 1;
                                            editor.putInt("orderNumber" , orderNumber);
                                            editor.commit();

                                            Toast.makeText(OrderInformation.this, "your order sent! ", Toast.LENGTH_SHORT).show();
                                            Confirmation.setEnabled(true);
                                            Intent intent = new Intent(OrderInformation.this, MyLocation.class);
                                            startActivity(intent);
                                        } else {
                                            Confirmation.setEnabled(true);
                                            Toast.makeText(OrderInformation.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                    Intent intent = new Intent(orderinformation.this, home.class);
//                    startActivity(intent);
            }
        });
    }

    void test (){

    }

}
