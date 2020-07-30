package com.example.wtf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


       ListView chatListView;

       EditText messageEditText;

       ArrayList<String> chatMessages;

       ArrayAdapter arrayAdapter;

       String activeUser;

       String selectedUserKey;

       String messageSent;

       FirebaseDatabase firebaseDatabase;

       FirebaseAuth firebaseAuth;

       public void send(View view){

         String finalString = firebaseAuth.getCurrentUser().getUid() + selectedUserKey;

         messageSent = messageEditText.getText().toString();

         Map<String , Object> messagesToSave = new HashMap<>();

         messagesToSave.put("Messages sent" , messageSent);

         firebaseDatabase.getReference().child("Users").child("Chats").child(finalString).push().setValue(messagesToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {


                 chatMessages.add(messageSent);

                 arrayAdapter.notifyDataSetChanged();

                 messageEditText.setText("");


             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

                 Toast.makeText(ChatActivity.this , e.getMessage() , Toast.LENGTH_SHORT).show();

             }
         });











     }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ChatActivity.this , usersListActivity.class);

        startActivity(intent);
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageEditText = findViewById(R.id.messageEditText);

        chatListView =  findViewById(R.id.chatListView);

        chatMessages = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(ChatActivity.this , android.R.layout.simple_expandable_list_item_1 , chatMessages);

        chatListView.setAdapter(arrayAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        activeUser =  intent.getStringExtra("Active user");

        selectedUserKey = intent.getStringExtra("User key");

        Log.i("Active user" , activeUser);

        setTitle("Chat with " +activeUser);



     }












}








