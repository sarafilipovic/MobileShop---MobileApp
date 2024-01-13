package com.example.mobileshop_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileshop_mobileapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        if(name.isEmpty() || surname.isEmpty() || emailUser.isEmpty() || passwordUser.isEmpty()){
            Toast.makeText(this, "Molimo popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }else{
            firebaseAuth.createUserWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(this,task->{
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    User member = new User(name,surname,emailUser,passwordUser);
                    String userId = user.getUid();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    databaseReference.child(user.getUid()).setValue(member);
                    Toast.makeText(this, "Uspjesno ste se registrirali", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    String error = task.getException().getMessage();
                    Log.e("RegisterActivity", "Registration failed" + task.getException().getMessage());
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
