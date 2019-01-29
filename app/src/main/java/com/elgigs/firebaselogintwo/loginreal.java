package com.elgigs.firebaselogintwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import es.dmoral.toasty.Toasty;

public class loginreal extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    TextInputEditText emailet, pwet;
    Button loginbtn;
    FirebaseAuth mAuthen;
    String emaillogin, passwordlogin;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuthen.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginreal);
        emailet = findViewById(R.id.login_email);
        pwet = findViewById(R.id.login_pw);
        loginbtn = findViewById(R.id.loginbtn);
        FirebaseApp.initializeApp(getApplicationContext());
        mAuthen = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(loginreal.this, dashboard.class));
                }
            }
        };

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("408640337422-9llj0pdaaht1ppd6lspbfco3kbdqt1ne.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailet.getText().toString().isEmpty() || !pwet.getText().toString().isEmpty()) {
                    emaillogin = emailet.getText().toString().toLowerCase().toLowerCase().trim();
                    passwordlogin = pwet.getText().toString().toLowerCase().trim();
                    mAuthen.signInWithEmailAndPassword(emaillogin, passwordlogin)
                            .addOnCompleteListener(loginreal.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
//                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        Toasty.success(getApplicationContext(), "Login Successful", Toasty.LENGTH_SHORT).show();
                                        Intent opendb = new Intent(loginreal.this, loginsuccessful.class);
                                        startActivity(opendb);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    public void GoogleSignIn(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuthen.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuthen.getCurrentUser();
                            Intent intent = new Intent(loginreal.this, loginsuccessful.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(loginreal.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginreal.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void fpw(View v) {
        Intent openfp = new Intent(loginreal.this, fp.class);
        startActivity(openfp);
    }

    public void signup(View v) {
        Intent openSignUp = new Intent(loginreal.this, signup.class);
        startActivity(openSignUp);
    }
    public void openpv(View v) {
        Intent openp = new Intent(loginreal.this, phone.class);
        startActivity(openp);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
