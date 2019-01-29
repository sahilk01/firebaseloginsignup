package com.elgigs.firebaselogintwo;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class phone extends AppCompatActivity {
    TextInputEditText phoneet;
    Button verifybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        phoneet = findViewById(R.id.phoneet);
        verifybtn = findViewById(R.id.verifybtn);
        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneet.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10) {
                    phoneet.setError("Please Enter a valid phone number");
                    phoneet.requestFocus();
                    return;
                }
                String phoneNumber = "+91" + number;

                Intent openpverification = new Intent(phone.this, pverification.class);
                openpverification.putExtra("phoneno", phoneNumber);
                startActivity(openpverification);
            }
        });
    }

}
