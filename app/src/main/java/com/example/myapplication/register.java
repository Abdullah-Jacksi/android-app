package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private EditText Username, Email, Password, Repassword, PhoneNumber;
    private Button Register;
    private TextView AlreadyHaveAccount;
    private ProgressDialog loadingBar;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private static final String USER_NUMBER = "UserNumber";
    int userNumberCounter = 0;

    private static final String ADMIN_NUMBER = "AdminNumber";
    int adminNumberCounter = 0;

    boolean isAnAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Username = (EditText) findViewById(R.id.username);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        Repassword = (EditText) findViewById(R.id.repassword);
        PhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        Register = (Button) findViewById(R.id.register);
        AlreadyHaveAccount = (TextView) findViewById(R.id.AlreadyHaveAccount);
        loadingBar = new ProgressDialog(this);
        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
        Switch onOffSwitch = (Switch)  findViewById(R.id.switch1);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                isAnAdmin = isChecked;
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAnAdmin){
                    Log.v("asd", "yess admin");
                    getOrderNumberForAdmin();
                }else{
                    getOrderNumberForUser();
                }
            }
        });
    }

    void saveToCache(String email) {
        sharedpreferences = getSharedPreferences("new", 0);
        editor = sharedpreferences.edit();
            editor.putString("logged_in", email);
            editor.commit();
    }

    void saveUserNameToCache(String userName) {
        sharedpreferences = getSharedPreferences("new", 0);
        editor = sharedpreferences.edit();
        editor.putString("userName", userName);
        editor.commit();
    }

    void saveUserNumberToCache(String userNumber2) {
        sharedpreferences = getSharedPreferences("new", 0);
        editor = sharedpreferences.edit();
        editor.putString("userNumber2", userNumber2);
        editor.commit();
    }

    //TODO: for user
    void getOrderNumberForUser() {

        loadingBar.setTitle("Create Account");
        loadingBar.setMessage("Please wait,while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Users").child(USER_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(USER_NUMBER) || dataSnapshot.child(USER_NUMBER).exists()) {
                    userNumberCounter = Integer.valueOf(String.valueOf(dataSnapshot.child(USER_NUMBER).getValue()));
//                    Toast.makeText(OrderInformation.this, String.valueOf(orderNumber+1), Toast.LENGTH_SHORT).show();
                    RegisterUser();
//                    Confirmation.setEnabled(true);
                } else {
                    HashMap<String, Object> orderNumberMap = new HashMap<>();
                    orderNumberMap.put(USER_NUMBER, 0);
                    RootRef.child("Users").child(USER_NUMBER).updateChildren(orderNumberMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Confirmation.setEnabled(true);
//                                        Toast.makeText(OrderInformation.this, "added new OrderNumber", Toast.LENGTH_SHORT).show();
//                                        setOrderDetailsForUser();
                                        RegisterUser();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(register.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
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
    private void RegisterUser() {
        String user = Username.getText().toString();
        String pass = Password.getText().toString();
        String repass = Repassword.getText().toString();
        String em = Email.getText().toString();
        String ph = PhoneNumber.getText().toString();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(register.this, "Please write your username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(em)) {
            Toast.makeText(register.this, "Please write your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(register.this, "Please write your password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(repass)) {
            Toast.makeText(register.this, "Please write your repassword", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ph)) {
            Toast.makeText(register.this, "Please write your phoneNumber", Toast.LENGTH_SHORT).show();
        } else {
            ValidateEmailForUser(user, pass, em, ph);
        }
    }
    private void ValidateEmailForUser(String user, String pass, String em, String ph) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean hasSame = false;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String,Object> mapObject =(HashMap<String, Object>)ds.getValue();
                    Log.d("asdasd" , String.valueOf(mapObject.containsValue("s")));
                    if(mapObject.containsValue(em)){
                        hasSame = true;
                    }
                }
                if (!hasSame) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("user", user);
                    userdataMap.put("Em", em);
                    userdataMap.put("Pass", pass);
                    userdataMap.put("Ph", ph);
                    RootRef.child("Users").child(String.valueOf(userNumberCounter + 1)).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        saveUserNumberToCache(String.valueOf(userNumberCounter + 1));
                                        Toast.makeText(register.this, "Congratulation,Your account has been created. ", Toast.LENGTH_SHORT).show();
                                        userNumberCounter += 1;
                                        updateOrderNumberForUser();
                                        saveUserNameToCache(user);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(register.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(register.this, "This " + em + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(register.this, "Please try again using another Email.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(register.this, login.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void updateOrderNumberForUser() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Users").child(USER_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> orderNumberMap = new HashMap<>();
                orderNumberMap.put(USER_NUMBER, userNumberCounter);
                RootRef.child("Users").child(USER_NUMBER).updateChildren(orderNumberMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(register.this, login.class);
                                    startActivity(intent);
//                                        Confirmation.setEnabled(true);
//                                    Toast.makeText(OrderInformation.this, "updated OrderNumber", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(OrderInformation.this, "your order sent! ", Toast.LENGTH_SHORT).show();
//                                    Confirmation.setEnabled(true);
//                                    Intent intent = new Intent(OrderInformation.this, MyLocation.class);
//                                    startActivity(intent);
//                                    getOrderNumberForAll();
                                } else {

                                    Toast.makeText(register.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //TODO: For admin
    void getOrderNumberForAdmin() {

        loadingBar.setTitle("Create Account");
        loadingBar.setMessage("Please wait,while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Admins").child(ADMIN_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(ADMIN_NUMBER) || dataSnapshot.child(ADMIN_NUMBER).exists()) {
                    adminNumberCounter = Integer.valueOf(String.valueOf(dataSnapshot.child(ADMIN_NUMBER).getValue()));
//                    Toast.makeText(OrderInformation.this, String.valueOf(orderNumber+1), Toast.LENGTH_SHORT).show();
                    RegisterAdmin();
//                    Confirmation.setEnabled(true);
                } else {
                    HashMap<String, Object> orderNumberMap = new HashMap<>();
                    orderNumberMap.put(ADMIN_NUMBER, 0);
                    RootRef.child("Admins").child(ADMIN_NUMBER).updateChildren(orderNumberMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                                        Confirmation.setEnabled(true);
//                                        Toast.makeText(OrderInformation.this, "added new OrderNumber", Toast.LENGTH_SHORT).show();
//                                        setOrderDetailsForUser();
                                        RegisterAdmin();
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(register.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
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
    private void RegisterAdmin() {
        String user = Username.getText().toString();
        String pass = Password.getText().toString();
        String repass = Repassword.getText().toString();
        String em = Email.getText().toString();
        String ph = PhoneNumber.getText().toString();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(register.this, "Please write your username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(em)) {
            Toast.makeText(register.this, "Please write your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(register.this, "Please write your password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(repass)) {
            Toast.makeText(register.this, "Please write your repassword", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ph)) {
            Toast.makeText(register.this, "Please write your phoneNumber", Toast.LENGTH_SHORT).show();
        } else {
            ValidateEmailForAdmin(user, pass, em, ph);
        }
    }
    private void ValidateEmailForAdmin(String user, String pass, String em, String ph) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean hasSame = false;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String,Object> mapObject =(HashMap<String, Object>)ds.getValue();
                    Log.d("asdasd" , String.valueOf(mapObject.containsValue("s")));
                   if(mapObject.containsValue(em)){
                       hasSame = true;
                   }
                }
                if (!hasSame) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("user", user);
                    userdataMap.put("Em", em);
                    userdataMap.put("Pass", pass);
                    userdataMap.put("Ph", ph);
                    RootRef.child("Admins").child(String.valueOf(adminNumberCounter + 1)).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("asd", String.valueOf(adminNumberCounter + 1));
                                        Toast.makeText(register.this, "Congratulation,Your account has been created. ", Toast.LENGTH_SHORT).show();
                                        adminNumberCounter += 1;
                                        updateOrderNumberForAdmin();
//                                        saveUserNameToCache(user);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(register.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(register.this, "This " + em + " already exists. ", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(register.this, "Please try again using another Email.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(register.this, login.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    void updateOrderNumberForAdmin() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Admins").child(ADMIN_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> orderNumberMap = new HashMap<>();
                orderNumberMap.put(ADMIN_NUMBER, adminNumberCounter);
                RootRef.child("Admins").child(ADMIN_NUMBER).updateChildren(orderNumberMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(register.this, login.class);
                                    startActivity(intent);
//                                        Confirmation.setEnabled(true);
//                                    Toast.makeText(OrderInformation.this, "updated OrderNumber", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(OrderInformation.this, "your order sent! ", Toast.LENGTH_SHORT).show();
//                                    Confirmation.setEnabled(true);
//                                    Intent intent = new Intent(OrderInformation.this, MyLocation.class);
//                                    startActivity(intent);
//                                    getOrderNumberForAll();
                                } else {
                                    Toast.makeText(register.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}