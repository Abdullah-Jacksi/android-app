package com.example.myapplication.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderInformation extends AppCompatActivity implements AdapterView.OnItemClickListener{


    ArrayList<String> myList, locationList;
    String radioValue, myEditText1Text, myEditText2Text;

    private Button Confirmation;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    String email = "";

    private static final String ORDER_NUMBER = "OrderNumber";
    Integer orderNumber = 0;

    private static final String TOTAL_ORDER_NUMBER = "TotalOrderNumber";
    Integer totalOrderNumber = 0;

    ProgressDialog progressDialog;

    TextView notes,address, amount, type, textView15;

    ListView listView;
    ArrayAdapter<String> ordersListAdapter;

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


        listView = findViewById(R.id.listViewOrder);
        listView.setOnItemClickListener(this);
        ordersListAdapter = new ArrayAdapter(OrderInformation.this, android.R.layout.simple_list_item_1,myList);
        listView.setAdapter(ordersListAdapter);


        notes = (TextView) findViewById(R.id.notesm);
        address= (TextView)findViewById(R.id.addressm);
        amount= (TextView)findViewById(R.id.amountm);
        type= (TextView)findViewById(R.id.typem);

        textView15 = (TextView)findViewById(R.id.textView15);
        if(radioValue.equals("Amount")){
            textView15.setText("The Amount");
        }else{
            textView15.setText("The Weight");
        }

        notes.setText(myEditText2Text);
        address.setText(locationList.get(0));
        amount.setText(myEditText1Text);
        type.setText(radioValue);

//                Toast.makeText(OrderInformation.this,myEditText2Text+" "+ myEditText1Text+" "+ radioValue+" "+myList.get(0), Toast.LENGTH_LONG).show();


        Confirmation = (Button) findViewById(R.id.Confirmation);
        Confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    Toast.makeText(OrderInformation.this, myList.size() + " " + radioValue + " " + myEditText1Text + " " + myEditText2Text, Toast.LENGTH_LONG).show();
//                    Toast.makeText(OrderInformation.this, locationList.get(0) + " " + locationList.get(1) + " " + locationList.get(2) + " " + locationList.get(3), Toast.LENGTH_LONG).show();

                myList.add("paper");
                myList.add("aluminum");
                radioValue = "Amount";
                myEditText1Text = "50";
                myEditText2Text = "another note";

                Confirmation.setEnabled(false);

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderInformation.this);
                builder.setTitle("Confirm your order!");
                builder.setMessage("Are you sure you want to send the order");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        progressDialog = new ProgressDialog(OrderInformation.this, R.style.DialogStyle); //new ProgressDialog(this);
                        progressDialog.setTitle("Please wait, it is sending your order");
                //        progressDialog.setMessage(" ");
                        progressDialog.setCancelable(true);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();

                        email = getEmailFromCache();
                        getOrderNumberForUser();
                        getOrderNumberForAll();
                    }
                });

                builder.setNegativeButton("back", null);
                builder.show();

            }
        });
    }


    String getEmailFromCache() {
        sharedpreferences = getSharedPreferences("new", 0);
        return sharedpreferences.getString("logged_in", "");
    }


    void getOrderNumberForUser() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(ORDER_NUMBER) || dataSnapshot.child(ORDER_NUMBER).exists()) {
                    orderNumber = Integer.valueOf(String.valueOf(dataSnapshot.child(ORDER_NUMBER).child(ORDER_NUMBER).getValue()));
//                    Toast.makeText(OrderInformation.this, String.valueOf(orderNumber+1), Toast.LENGTH_SHORT).show();
                    setOrderDetailsForUser();
//                    Confirmation.setEnabled(true);
                } else {
                    HashMap<String, Object> orderNumberMap = new HashMap<>();
                    orderNumberMap.put(ORDER_NUMBER, 0);
                    RootRef.child("Users").child(email).child(ORDER_NUMBER).updateChildren(orderNumberMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Confirmation.setEnabled(true);
//                                        Toast.makeText(OrderInformation.this, "added new OrderNumber", Toast.LENGTH_SHORT).show();
                                        setOrderDetailsForUser();
                                    } else {

                                        Toast.makeText(OrderInformation.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void setOrderDetailsForUser() {
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
                userdataMap.put("status", "قيد الانتظار");

//                String key = RootRef.child("Users").child(email).child("Orders").push().getKey();
                RootRef.child("Users").child(email).child("Orders").child(String.valueOf(orderNumber + 1)).updateChildren(userdataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    orderNumber += 1;
                                    updateOrderNumberForUser();
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
    }

    void updateOrderNumberForUser() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> orderNumberMap = new HashMap<>();
                orderNumberMap.put(ORDER_NUMBER, orderNumber);
                RootRef.child("Users").child(email).child(ORDER_NUMBER).updateChildren(orderNumberMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                        Confirmation.setEnabled(true);
//                                    Toast.makeText(OrderInformation.this, "updated OrderNumber", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(OrderInformation.this, "your order sent! ", Toast.LENGTH_SHORT).show();
//                                    Confirmation.setEnabled(true);
//                                    Intent intent = new Intent(OrderInformation.this, MyLocation.class);
//                                    startActivity(intent);
//                                    getOrderNumberForAll();
                                } else {

                                    Toast.makeText(OrderInformation.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void getOrderNumberForAll() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(TOTAL_ORDER_NUMBER) || dataSnapshot.child(TOTAL_ORDER_NUMBER).exists()) {
                    totalOrderNumber = Integer.valueOf(String.valueOf(dataSnapshot.child(TOTAL_ORDER_NUMBER).child(TOTAL_ORDER_NUMBER).getValue()));
//                    Toast.makeText(OrderInformation.this, String.valueOf(totalOrderNumber+1), Toast.LENGTH_SHORT).show();
                    setOrderDetailsForAll();
//                    Confirmation.setEnabled(true);
                } else {
                    HashMap<String, Object> orderNumberMap = new HashMap<>();
                    orderNumberMap.put(TOTAL_ORDER_NUMBER, 0);
                    RootRef.child(TOTAL_ORDER_NUMBER).updateChildren(orderNumberMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Confirmation.setEnabled(true);
//                                        Toast.makeText(OrderInformation.this, "added new OrderNumber", Toast.LENGTH_SHORT).show();
                                        setOrderDetailsForAll();
                                    } else {

                                        Toast.makeText(OrderInformation.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void setOrderDetailsForAll() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("totalOrderNumber", totalOrderNumber + 1);
                userdataMap.put("myList", myList);
                userdataMap.put("locationList", locationList);
                userdataMap.put("radioValue", radioValue);
                userdataMap.put("myEditText1Text", myEditText1Text);
                userdataMap.put("myEditText2Text", myEditText2Text);
                userdataMap.put("status", "قيد الانتظار");
//                String key = RootRef.child("Users").child(email).child("Orders").push().getKey();
                RootRef.child("AllOrders").child(String.valueOf(totalOrderNumber + 1)).updateChildren(userdataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    totalOrderNumber += 1;
                                    updateOrderNumberForAll();
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
    }

    void updateOrderNumberForAll() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> orderNumberMap = new HashMap<>();
                orderNumberMap.put(TOTAL_ORDER_NUMBER, totalOrderNumber);
                RootRef.child(TOTAL_ORDER_NUMBER).updateChildren(orderNumberMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                        Confirmation.setEnabled(true);
//                                    Toast.makeText(OrderInformation.this, "updated OrderNumber", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(OrderInformation.this, "your order sent! ", Toast.LENGTH_SHORT).show();
                                    Confirmation.setEnabled(true);
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(OrderInformation.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(OrderInformation.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void saveToCache() {
        sharedpreferences = getSharedPreferences("new", 0);
        editor = sharedpreferences.edit();
        if (!sharedpreferences.contains(ORDER_NUMBER)) {
            Toast.makeText(OrderInformation.this, "not exist orderNumber", Toast.LENGTH_SHORT).show();
            editor.putInt(ORDER_NUMBER, 0);
            editor.commit();
        } else {
            Toast.makeText(OrderInformation.this, "yes exist orderNumber", Toast.LENGTH_SHORT).show();
            orderNumber += sharedpreferences.getInt(ORDER_NUMBER, 0);
        }
        Toast.makeText(OrderInformation.this, String.valueOf(orderNumber), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
