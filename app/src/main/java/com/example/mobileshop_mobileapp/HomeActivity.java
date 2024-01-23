package com.example.mobileshop_mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop_mobileapp.adapters.MobileAdapter;
import com.example.mobileshop_mobileapp.model.Mobile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private MobileAdapter mobileAdapter;

    private DrawerLayout drawer;
    private FirebaseAuth auth;
    private List<Mobile> mobileList = new ArrayList<>();
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



        RecyclerView recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("mobile");

        mobileAdapter = new MobileAdapter(mobileList, menuRef);
        recyclerView.setAdapter(mobileAdapter);

        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Mobile> allMenuList = new ArrayList<>();

                for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                    Mobile menu = menuSnapshot.getValue(Mobile.class);
                    if (menu != null) {
                        allMenuList.add(menu);
                        mobileAdapter.notifyDataSetChanged();
                    }
                }

                RecyclerView recyclerView = findViewById(R.id.recyclerViewMenu);
                MobileAdapter mobileAdapter = new MobileAdapter(allMenuList, menuRef);
                recyclerView.setAdapter(mobileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    public void mobileBtn(View view){
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("menu");
        menuRef.orderByChild("category").equalTo("food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Mobile> filteredMenuList = new ArrayList<>();

                for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                    Mobile menu = menuSnapshot.getValue(Mobile.class);
                    if (menu != null) {
                        filteredMenuList.add(menu);
                    }
                }
                RecyclerView recyclerView = findViewById(R.id.recyclerViewMenu);
                MobileAdapter mobileAdapter = new MobileAdapter(filteredMenuList,menuRef);
                recyclerView.setAdapter(mobileAdapter);
                mobileAdapter.updateData(filteredMenuList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Obrada grešaka
            }
        });
    }




    public void logoutUser(){
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        Toast.makeText(this, "Uspješno ste se odjavili", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
            mobileBtn(null);
        }else if (id == CART_MENU_ID){
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

    public void addMobile(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_mobile, null);

        EditText imeMobitela = dialogView.findViewById(R.id.nazivMobitela);
        EditText model = dialogView.findViewById(R.id.modelMobitela);
        EditText cijena = dialogView.findViewById(R.id.cijenaMobitela);
        EditText slika = dialogView.findViewById(R.id.slikaMobitela);

        slika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        builder.setView(dialogView)
                .setTitle("Add item")
                .setPositiveButton("OK", null) // Ne postavljamo OnClickListener ovdje
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Logika za negativan odgovor
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = imeMobitela.getText().toString();
                        String model_mobile = model.getText().toString();
                        String price_mobile  = cijena.getText().toString();

                        if (name.isEmpty()) {
                            Toast.makeText(view.getContext(), "Ime je obavezno", Toast.LENGTH_SHORT).show();
                        } else if (price_mobile.isEmpty()) {
                            Toast.makeText(view.getContext(), "Cijena je obavezna", Toast.LENGTH_SHORT).show();
                        } else if (model_mobile.isEmpty()) {
                            Toast.makeText(view.getContext(), "Model je obavezan", Toast.LENGTH_SHORT).show();
                        } else {
                            if (imageUri != null) {
                                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("images");
                                StorageReference imageRef = mStorageRef.child(UUID.randomUUID().toString());

                                try {
                                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                    UploadTask uploadTask = imageRef.putStream(inputStream);
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl = uri.toString();

                                                    DatabaseReference base = FirebaseDatabase.getInstance().getReference("mobile");
                                                    String itemId = base.push().getKey();
                                                    Mobile mobile = new Mobile(itemId, name, model_mobile, price_mobile, imageUrl);
                                                    base.child(itemId).setValue(mobile);


                                                    Toast.makeText(HomeActivity.this, "Mobitel dodan", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(view.getContext(), "Slika je obavezna", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        dialog.show();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }

}



