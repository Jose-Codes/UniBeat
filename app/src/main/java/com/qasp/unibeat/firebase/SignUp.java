package com.qasp.unibeat.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.qasp.unibeat.MainActivity;
import com.qasp.unibeat.R;

public class SignUp extends AppCompatActivity {

    EditText edtEmail;
    EditText edtName;
    EditText edtLocation;
    ImageButton btnSignUp;
    ImageButton btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtLocation = findViewById(R.id.edtLocation);
        btnSignUp = findViewById(R.id.btnEditProfile);
        btnSignOut = findViewById(R.id.btnSignOut);
        MainActivity mainActivity = MainActivity.getInstance();
        Intent i = new Intent(SignUp.this, MainActivity.class);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
                mainActivity.signOut();
                mainActivity.signIn();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                i.putExtra("Email", edtEmail.getText().toString());
                i.putExtra("Name", edtName.getText().toString());
                i.putExtra("Location", edtLocation.getText().toString());
                Toast.makeText(SignUp.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }
}