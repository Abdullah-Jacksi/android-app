package com.example.myapplication.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.OrderModel;
import com.example.myapplication.R;
import com.example.myapplication.login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AdminHomeScreen extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> ordersListAdapter;
    ProgressDialog progressDialog;
    Button LogOutButtonAdmin;

    ArrayList<OrderModel> ordersList;
    ArrayList<String> orderDetails = new ArrayList<String>();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);

        listView = findViewById(R.id.listViewAdmin);

        LogOutButtonAdmin = findViewById(R.id.LogOutButtonAdmin);
        LogOutButtonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferences = getSharedPreferences("new", 0);
                editor = sharedpreferences.edit();
                editor.remove("user_type");
                editor.apply();
                Intent intent = new Intent(AdminHomeScreen.this, login.class);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(this, R.style.DialogStyle); //new ProgressDialog(this);
//        progressDialog.setTitle("Have a nice time");
//        progressDialog.setMessage(" ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
//
        getOrdersOfUser();



    }


    void getOrdersOfUser() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("AllOrders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()){

                        ordersList =new ArrayList<OrderModel>();

                        Map<String,Object> mapObject =(HashMap<String, Object>)ds.getValue();


                        OrderModel order = new OrderModel();
                        order.setMyEditText1Text(String.valueOf(mapObject.get("myEditText1Text")));
                        order.setMyEditText2Text(String.valueOf(mapObject.get("myEditText2Text")));
                        order.setOrderNumber(String.valueOf(mapObject.get("totalOrderNumber")));
                        order.setStatus(String.valueOf(mapObject.get("status")));
                        order.setRadioValue(String.valueOf(mapObject.get("radioValue")));
                        order.setMyList(new ArrayList<String>((Collection<String>)mapObject.get("myList")));
                        order.setLocationList(new ArrayList<String>((Collection<String>)mapObject.get("locationList")));

                        Log.d("TAG",  String.valueOf( order.getLocationList().get(0)));

                        ordersList.add(order);

                        orderDetails.add("Order Number is : " + order.getOrderNumber() +  "\nThe order status is : " + order.getStatus() );

                    }

                }
                Toast.makeText(AdminHomeScreen.this, String.valueOf(orderDetails.size()), Toast.LENGTH_SHORT).show();

                ordersListAdapter = new ArrayAdapter<String>(AdminHomeScreen.this, android.R.layout.simple_list_item_1, orderDetails);

                listView.setAdapter(ordersListAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminHomeScreen.this, String.valueOf(error), Toast.LENGTH_LONG).show();

//                progressBar.setVisibility(View.GONE);
            }
        });
    }

}