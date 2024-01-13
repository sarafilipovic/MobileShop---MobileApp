package com.example.mobileshop_mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText novaLozinka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        TextView emailTextView = findViewById(R.id.userEmail);
        TextView nameTextView = findViewById(R.id.userName);

        if(currentUser != null){
            String email = currentUser.getEmail();
            emailTextView.setText(email);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("firstName");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstName = snapshot.getValue().toString();
                    nameTextView.setText(firstName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AccountActivity.this, "Greska", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    public void confirmReset(View view) {
        FirebaseUser user = auth.getCurrentUser();
        if (novaLozinka != null) {
            String password = novaLozinka.getText().toString();

            assert user != null;
            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AccountActivity.this, "Uspješno ste promijenili lozinku", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AccountActivity.this, "Greška prilikom promjene lozinke", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(AccountActivity.this, "EditText za lozinku je null", Toast.LENGTH_SHORT).show();
        }
    }
}
