package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;




public class OrdersHistory extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    String email  = "";

    ArrayList<String> myList, locationList;
    String radioValue, myEditText1Text, myEditText2Text, orderNumber, status;

    ListView listView;
    ArrayAdapter<String> ordersListAdapter;
    ProgressDialog progressDialog;

    ArrayList<OrderModel> ordersList;
    ArrayList<String> orderDetails = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);

        listView = findViewById(R.id.listView);

        progressDialog = new ProgressDialog(this, R.style.DialogStyle); //new ProgressDialog(this);
//        progressDialog.setTitle("Have a nice time");
//        progressDialog.setMessage(" ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
//
        email = getEmailFromCache ();

        getOrdersOfUser();



    }

    String getEmailFromCache (){
        sharedpreferences = getSharedPreferences("new", 0);
        return sharedpreferences.getString("logged_in", "");
    }


    void getOrdersOfUser() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Users").child(email).child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.exists()){

                        ordersList =new ArrayList<OrderModel>();

                        Map<String,Object> mapObject =(HashMap<String, Object>)ds.getValue();


                        OrderModel order = new OrderModel();
                        order.setMyEditText1Text(String.valueOf(mapObject.get("myEditText1Text")));
                        order.setMyEditText2Text(String.valueOf(mapObject.get("myEditText2Text")));
                        order.setOrderNumber(String.valueOf(mapObject.get("orderNumber")));
                        order.setStatus(String.valueOf(mapObject.get("status")));
                        order.setRadioValue(String.valueOf(mapObject.get("radioValue")));

                        order.setMyList(new ArrayList<String>((Collection<String>)mapObject.get("myList")));
                        order.setLocationList(new ArrayList<String>((Collection<String>)mapObject.get("locationList")));

                        Log.d("TAG",  String.valueOf( order.getLocationList().get(0)));

                        ordersList.add(order);

                        orderDetails.add("Order Number is : " + order.getOrderNumber() +  "\nThe order status is : " + order.getStatus() );

                    }

                }
                Toast.makeText(OrdersHistory.this, String.valueOf(orderDetails.size()), Toast.LENGTH_SHORT).show();

                ordersListAdapter = new ArrayAdapter<String>(OrdersHistory.this, android.R.layout.simple_list_item_1, orderDetails);

                listView.setAdapter(ordersListAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrdersHistory.this, String.valueOf(error), Toast.LENGTH_LONG).show();

//                progressBar.setVisibility(View.GONE);
            }
        });
    }


}
