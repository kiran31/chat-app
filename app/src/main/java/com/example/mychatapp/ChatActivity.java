package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ImageView backimage;
    FloatingActionButton fab;
    EditText editTextMsg;
    TextView textViewChat;
    RecyclerView recyclerView;
    String userName, otherName;
    FirebaseDatabase database;
    DatabaseReference reference;
    MessageAdapter adapter;
    List<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backimage = findViewById(R.id.backimage);
        fab = findViewById(R.id.fab);
        editTextMsg = findViewById(R.id.editTextTextMultiLine);
        textViewChat = findViewById(R.id.textViewChat);
        recyclerView = findViewById(R.id.chatRecyclerView);

        userName = getIntent().getStringExtra("userName");
        otherName = getIntent().getStringExtra("otherName");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();


        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        textViewChat.setText(otherName);
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = editTextMsg.getText().toString();
                if(!msg.equals("")){
                    sendMessage(msg);
                    editTextMsg.setText("");
                }
            }
        });

        getMassages();
    }

    private void getMassages() {
        reference.child("Messages").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                try {
                    ModelClass modelClass = dataSnapshot.getValue(ModelClass.class);
                    list.add(modelClass);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(list.size()-1);
                }catch (Exception e){
                   e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new MessageAdapter(list, userName);
        recyclerView.setAdapter(adapter);
    }

    private void sendMessage(String msg) {
        String key = reference.child("Messages").child(userName).child(otherName).push().getKey();
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", msg);
        messageMap.put("from", userName);
        reference.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    reference.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
                }
            }
        });
    }
}