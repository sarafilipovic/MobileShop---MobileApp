package com.example.mobileshop_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop_mobileapp.adapters.KosaricaAdapter;
import com.example.mobileshop_mobileapp.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class KosaricaActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    private DatabaseReference cartRef;
    private RecyclerView recyclerViewCart;
    private KosaricaAdapter cartAdapter;
    private TextView textViewCartEmpty;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kosarica);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                return true;
            }
        });


        recyclerViewCart = findViewById(R.id.recyclerViewMenu);


        // Firebase initialization
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

            // Initialize RecyclerView and Adapter
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
            cartAdapter = new KosaricaAdapter(new ArrayList<>(), cartRef);
            recyclerViewCart.setAdapter(cartAdapter);

            // Listen for changes in the cart
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Cart> updatedCartItemList = new ArrayList<>();

                    for (DataSnapshot cartItemSnapshot : dataSnapshot.getChildren()) {
                        Cart cartItem = cartItemSnapshot.getValue(Cart.class);
                        if (cartItem != null) {
                            updatedCartItemList.add(cartItem);
                        }
                    }


                    cartAdapter.updateData(updatedCartItemList);


                    if (updatedCartItemList.isEmpty()) {
                        recyclerViewCart.setVisibility(View.GONE);
                    } else {
                        recyclerViewCart.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("CartItem", "Error fetching cart data: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("CartItem", "User not logged in");
        }
    }



    public void handleNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        final int ACCOUNT_MENU_ID = R.id.account;
        final int LOGOUT_MENU_ID = R.id.logout;
        final int MOBILE_MENU_ID = R.id.mobile;
        final int CART_MENU_ID = R.id.kosarica;

        if (id == ACCOUNT_MENU_ID) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }else if(id == LOGOUT_MENU_ID){
            logoutUser();
        }else if(id == MOBILE_MENU_ID){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else if(id == CART_MENU_ID){
            Intent intent = new Intent(this, KosaricaActivity.class);
            startActivity(intent);
        }


    }

    public void openDrawer(View view) {
        drawer.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void logoutUser(){
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Toast.makeText(this, "Uspješno ste se odjavili", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}