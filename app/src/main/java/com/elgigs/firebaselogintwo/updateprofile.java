package com.elgigs.firebaselogintwo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class updateprofile extends AppCompatActivity {
    EditText personName, pnum;
    Button updateProfilebtn;
    String currentUser;
    String namePerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        personName = findViewById(R.id.name);
        pnum = findViewById(R.id.phone_number);
        updateProfilebtn = findViewById(R.id.update_btn);

        FirebaseApp.initializeApp(updateprofile.this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try {
            currentUser = user.getUid();
        }catch (Exception e) {

        }
        if (currentUser != null) {
            Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "user not found", Toast.LENGTH_SHORT).show();

        }
        updateProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namePerson = personName.getText().toString();
                String phoneNumber = pnum.getText().toString();
                Toast.makeText(updateprofile.this, namePerson, Toast.LENGTH_SHORT).show();
                if (!namePerson.isEmpty()){
                    User user = new User(namePerson, phoneNumber);
                    FirebaseDatabase.getInstance().getReference("Users").child(currentUser)
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(updateprofile.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    Toast.makeText(updateprofile.this, "Please enter full name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
