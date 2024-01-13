package com.example.mobileshop_mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class );
                startActivity(intent);
            }
        });

    }

    public void loginUser(View view) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.emailUser);
        EditText password = findViewById(R.id.passwordUser);

        String emailUser = email.getText().toString();
        String passwordUser = password.getText().toString();

        if(emailUser.isEmpty() || passwordUser.isEmpty()){
            Toast.makeText(this, "Molimo popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }else{
            auth.signInWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Uspijesno ste se prijavili", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent (MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else{
                        String error = task.getException().getMessage();
                        Log.e("Login error", error);
                        Toast.makeText(MainActivity.this, "Prijava nije uspijesna", Toast.LENGTH_SHORT).show();
                    }

                }

            });
        }
    }
}