package com.example.myapplication;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class register extends AppCompatActivity {
    private EditText Username, Email, Password, Repassword, PhoneNumber;
    private Button Register;
    private TextView AlreadyHaveAccount;
    private ProgressDialog loadingBar;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

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
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterUser();
            }
        });
    }

    void saveToCache(String email) {
        sharedpreferences = getSharedPreferences("new", 0);
        editor = sharedpreferences.edit();
            editor.putString("logged_in", email);
            editor.commit();
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
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait,while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidateEmail(user, pass, em, ph);
        }
    }
    private void ValidateEmail(String user, String pass, String em, String ph) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(em).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("user", user);
                    userdataMap.put("Em", em);
                    userdataMap.put("Pass", pass);
                    userdataMap.put("Ph", ph);
                    RootRef.child("Users").child(em).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(register.this, "Congratulation,Your account has been created. ", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(register.this, login.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(register.this, "Network Error:please try again after some time ...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(register.this, "This" + em + "already exists.", Toast.LENGTH_SHORT).show();
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
}