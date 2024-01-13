package com.example.mobileshop_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                return true;
            }
        });

    }



    public void handleNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        final int ACCOUNT_MENU_ID = R.id.account;
        final int LOGOUT_MENU_ID = R.id.logout;

        if (id == ACCOUNT_MENU_ID) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }else if(id == LOGOUT_MENU_ID){
            logoutUser();
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
}