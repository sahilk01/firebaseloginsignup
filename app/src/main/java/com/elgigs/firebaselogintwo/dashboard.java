package com.elgigs.firebaselogintwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class dashboard extends AppCompatActivity {
    Button signOut;
    FirebaseAuth mAuth;
    EditText userName, userPhone;
    ImageView imageView;
    String name, phone, namePerson;
    FirebaseDatabase database;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        signOut = findViewById(R.id.signout);
        userName = findViewById(R.id.name);
        userPhone = findViewById(R.id.phone);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        imageView = findViewById(R.id.imageview);
        Picasso.get().load("https://elgigs.com/wp-content/uploads/2018/11/download.png").into(imageView);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("fullName").getValue().toString();
                phone = dataSnapshot.child("phoneNumber").getValue().toString();
                userName.setText(name);
                userPhone.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(), databaseError.getMessage(), Toasty.LENGTH_SHORT).show();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(dashboard.this, loginreal.class));
                }
            }
        };

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    public void openUpdateProfile(View v) {
        namePerson = userName.getText().toString();
        String phoneNumber = userPhone.getText().toString();
        if (!namePerson.isEmpty()){
            User user = new User(namePerson, phoneNumber);
            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid())
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                }
            });
        }else
            Toast.makeText(getApplicationContext(), "Please enter full name", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
