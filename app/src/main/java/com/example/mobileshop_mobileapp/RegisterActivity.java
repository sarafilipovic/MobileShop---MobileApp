package com.example.mobileshop_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reigster);

        firebaseAuth = FirebaseAuth.getInstance();
        TextView loginBtn = findViewById(R.id.loginBtn);
        Button registerButton = findViewById(R.id.registerButton);


       loginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
               startActivity(intent);
           }
       });


    }

    public void registerButton(View view){
        EditText firstName = findViewById(R.id.first_name);
        EditText lastName = findViewById(R.id.last_name);
        EditText email = findViewById(R.id.emailUser);
        EditText password = findViewById(R.id.passwordUser);

        String name = firstName.getText().toString();
        String surname = lastName.getText().toString();
        String emailUser = email.getText().toString();
        String passwordUser = password.getText().toString();
    }
}
