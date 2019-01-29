package com.elgigs.firebaselogintwo;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class fp extends AppCompatActivity {
    TextInputEditText mail;
    Button rp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp);
        mail = findViewById(R.id.femail);
        rp = findViewById(R.id.rpbtn);
        auth = FirebaseAuth.getInstance();
        rp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredMail = mail.getText().toString().toLowerCase().trim();
                if (enteredMail.isEmpty()) {
                    mail.setError("Enter a valid email address");
                } else {
                    auth.sendPasswordResetEmail(enteredMail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toasty.info(getApplicationContext(), "Please Check Your Email", Toasty.LENGTH_SHORT).show();
//                                        Toast.makeText(fp.this, "Please Check your email", Toast.LENGTH_SHORT).show();
                                    }   
                                    else {
                                        Toast.makeText(fp.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
