package com.example.myapplication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class items extends AppCompatActivity implements View.OnClickListener {
    Button paper;
    Button plastic;
    Button glass;
    Button aluminum;
    Button next, profile,orderHistory , logOut;

    ArrayList<String> list = new ArrayList<String>();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
        paper = findViewById(R.id.paper);
        plastic = findViewById(R.id.plastic);
        glass = findViewById(R.id.glass);
        aluminum = findViewById(R.id.aluminum);
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String s = list.size();
//
                if(list.size() == 0 || list.isEmpty()){
                    Toast.makeText(items.this, "you should select one at least", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(items.this, OrderDetails.class);
                    intent.putExtra("myList"  , list);
                    startActivity(intent);
                }

            }
        });
        paper.setOnClickListener(this);
        plastic.setOnClickListener(this);
        glass.setOnClickListener(this);
        aluminum.setOnClickListener(this);

        profile = findViewById(R.id.profileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(items.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        orderHistory = findViewById(R.id.orderHistoryButton);
        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        logOut = findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferences = getSharedPreferences("new", 0);
                editor = sharedpreferences.edit();
                editor.remove("logged_in");
                Intent intent = new Intent(items.this, login.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.paper:
                paper.setText("Paper is Selected");
                paper.setBackgroundColor(Color.parseColor("#327735"));
                list.add("paper");
                break;
            case R.id.plastic:
                plastic.setText("Plastic is Selected");
                plastic.setBackgroundColor(Color.parseColor("#327735"));
                list.add("plastic");
                break;
            case R.id.glass:
                glass.setText("Glass is Selected");
                glass.setBackgroundColor(Color.parseColor("#327735"));
                list.add("glass");
                break;
            case R.id.aluminum:
                aluminum.setText("Aluminum is Selected");
                aluminum.setBackgroundColor(Color.parseColor("#327735"));
                list.add("aluminum");
                break;

        }
    }

}






