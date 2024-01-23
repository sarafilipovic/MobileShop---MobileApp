package com.example.mobileshop_mobileapp.adapters;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobileshop_mobileapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop_mobileapp.model.Cart;
import com.example.mobileshop_mobileapp.model.Mobile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.MobileViewHolder> {
    private int selectedPosition = RecyclerView.NO_POSITION;
    private List<Mobile> mobileList;

    private DatabaseReference menuRef;
    public MobileAdapter(List<Mobile> mobileList,DatabaseReference menuRef) {
        this.mobileList = mobileList;
        this.menuRef = menuRef;
    }

    @NonNull
    @Override
    public MobileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_item, parent, false);
        final MobileViewHolder viewHolder = new MobileViewHolder(view);
        Log.d("ViewHolderCreation", "onCreateViewHolder called");
        return viewHolder;
    }

    public void updateData(List<Mobile> newMobileList) {
        this.mobileList = newMobileList;
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull MobileViewHolder holder, int position) {
        Mobile mobile = mobileList.get(position);
        final int itemPosition = position;
        holder.textViewName.setText(mobile.getImeMobitela());
        holder.textViewPrice.setText(mobile.getCijena());

        if (mobile.getSlika() != null && !mobile.getSlika().isEmpty()) {
            ImageView imageView = holder.itemView.findViewById(R.id.imageView);
            Glide.with(holder.itemView.getContext())
                    .load(mobile.getSlika())
                    .into(imageView);

        }

        holder.deleteMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemMobile(mobile.getId());
            }
        });

        holder.dodajKosaricu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(mobile);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mobileList.size();

    }



    private void deleteItemMobile(String mobileid) {


        if (menuRef != null) {
            menuRef.child(mobileid).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    List<Mobile> updatedMenuList = new ArrayList<>();

                                    for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                                        Mobile menu = menuSnapshot.getValue(Mobile.class);
                                        if (menu != null) {
                                            updatedMenuList.add(menu);
                                        }
                                    }
                                    updateData(updatedMenuList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    public static class MobileViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        ImageView deleteMobile;
        ImageView dodajKosaricu;


        public MobileViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            deleteMobile = itemView.findViewById(R.id.deleteMobile);
            dodajKosaricu = itemView.findViewById(R.id.dodajKosaricu);
            Log.d("ViewHolderCreation", "ViewHolder created");


        }


    }

    private void addToCart(Mobile mobile) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

            String itemId = mobile.getId();
            String itemName = mobile.getImeMobitela();
            String modelItem = mobile.getModel();
            String itemPrice = mobile.getCijena();
            String imageUrl = mobile.getSlika();
            Log.e("Cart", "ItemID: " + itemId);
            Log.e("Cart", "Naziv: " + itemName);
            Log.e("Cart", "Cijena: " + itemPrice);
            Cart cartItem = new Cart(itemId, itemName, modelItem, itemPrice, imageUrl);

            String path = cartRef.child(itemId).toString();
            Log.e("Cart", "Putanja u košarici: " + path);

            cartRef.child(itemId).setValue(cartItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Cart", "Uspješno dodano u košaricu");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Cart", "Greška prilikom dodavanja u košaricu: " + e.getMessage());
                        }
                    });
        } else {
            Log.e("Cart", "Korisnik nije prijavljen");
        }
    }



}