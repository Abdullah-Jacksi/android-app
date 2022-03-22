package com.example.myapplication;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrderInformation extends AppCompatActivity {


    ArrayList<String> myList,locationList;
    String radioValue, myEditText1Text, myEditText2Text;

    private Button Confirmation;
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


        Confirmation = (Button) findViewById(R.id.Confirmation);
        Confirmation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Intent intent = new Intent(orderinformation.this, home.class);
//                    startActivity(intent);
                }
            });
        }

}
