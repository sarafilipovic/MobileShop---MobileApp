package com.example.mobileshop_mobileapp.adapters;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.mobileshop_mobileapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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


}