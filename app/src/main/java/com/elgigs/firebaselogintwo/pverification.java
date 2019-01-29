package com.elgigs.firebaselogintwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class pverification extends AppCompatActivity {
    private String verificationid;
    Button fverify;
    EditText enterotp;
    FirebaseAuth mAuth;
    TextView pnotv;
    String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pverification);
        mAuth = FirebaseAuth.getInstance();
        enterotp = findViewById(R.id.otp);
        phoneno = getIntent().getStringExtra("phoneno");
        pnotv = findViewById(R.id.phtv);
        fverify = findViewById(R.id.finalverifybtn);
        pnotv.setText(phoneno);
        doVerification(phoneno);
        fverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = enterotp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    enterotp.setError("Enter Valid OTP");
                    enterotp.requestFocus();
                    return;
                }
                    verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()) {
                       Intent openDashboard = new Intent(pverification.this, loginsuccessful.class);
                       openDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(openDashboard);
                   } else {
                       Toast.makeText(pverification.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }
                    }
                });
    }

    private void doVerification(String phoneno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneno,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(pverification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
