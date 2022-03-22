package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {

   Button Next;
   String radioValue,myEditText1Text,myEditText2Text;
   RadioGroup radioGroup;
   RadioButton radioButton;
   EditText myEditText1,myEditText2;
    ArrayList<String> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetails);
        myList = (ArrayList<String>) getIntent().getSerializableExtra("myList");

//        Toast.makeText(OrderDetails.this,myList1.get(0) , Toast.LENGTH_LONG).show();

        Next = (Button) findViewById(R.id.next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEditText1 =  (EditText) findViewById(R.id.editTextTextPersonName3);
                myEditText1Text = myEditText1.getText().toString();
                myEditText2 =  (EditText) findViewById(R.id.editTextTextPersonName);
                myEditText2Text = myEditText2.getText().toString();

                Toast.makeText(OrderDetails.this,radioValue , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(OrderDetails.this, MyLocation.class);
                intent.putExtra("myList"  , myList);
                intent.putExtra("radioValue", radioValue);
                intent.putExtra("myEditText1Text", myEditText1Text);
                intent.putExtra("myEditText2Text", myEditText2Text);

                startActivity(intent);
            }
        });
    }

    public void checkButton(View v) {

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        radioValue = radioButton.getText().toString();
//        Toast.makeText(this,radioValue , Toast.LENGTH_SHORT).show();
    }
}