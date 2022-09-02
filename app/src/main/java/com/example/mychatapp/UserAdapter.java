package com.example.mychatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<String> userList;
    String username;
    Context context;

    FirebaseDatabase database;
    DatabaseReference reference;

    public UserAdapter(List<String> userList, String username, Context context) {
        this.userList = userList;
        this.username = username;
        this.context = context;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        reference.child("Users").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String otherName = dataSnapshot.child("userName").getValue().toString();
                String imageUrl = dataSnapshot.child("image").getValue().toString();
                holder.textViewUser.setText(otherName);
                if (imageUrl.equals("null")){
                    holder.imageViewUser.setImageResource(R.drawable.ic_baseline_person_24);
                }else {
                    Picasso.get().load(imageUrl).into(holder.imageViewUser);
                }

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("userName", username);
                        intent.putExtra("otherName", otherName);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageViewUser;
        TextView textViewUser;
        CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUser = itemView.findViewById(R.id.imageViewUser);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
