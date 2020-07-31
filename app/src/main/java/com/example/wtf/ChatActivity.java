package com.example.wtf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


       ListView chatListView;

       EditText messageEditText;

       ArrayList<String> chatMessages;

      ArrayList<String> keys;

       ArrayAdapter arrayAdapter;

       String activeUser;

       String selectedUserKey;

       String messageSent;

       FirebaseDatabase firebaseDatabase;

       FirebaseAuth firebaseAuth;

       String finalString, id1 , id2;

       public void send(View view){



           messageSent = messageEditText.getText().toString();

           if(messageSent.isEmpty()){

               return;

           }

           else {

               Map<String, Object> messagesToSave = new HashMap<>();

               messagesToSave.put("Messages sent", messageSent);

               firebaseDatabase.getReference().child("Chats").child(finalString).push().setValue(messagesToSave);

               messageEditText.setText("");



           }
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

        keys = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(ChatActivity.this , R.layout.row , chatMessages);

        chatListView.setAdapter(arrayAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        activeUser =  intent.getStringExtra("Active user");

        selectedUserKey = intent.getStringExtra("User key");

        Log.i("Active user" , activeUser);

        setTitle("Chat with " +activeUser);

        id1 = firebaseAuth.getCurrentUser().getUid();

        id2= selectedUserKey;

        final ArrayList<String> newArrayList = new ArrayList<>();

        newArrayList.add(id1);

        newArrayList.add( id2);

        Collections.sort(newArrayList);

        finalString = newArrayList.get(0)+"_"+newArrayList.get(1);

        firebaseDatabase.getReference().child("Chats").child(finalString).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String messagesFromDatabse = snapshot.child("Messages sent").getValue(String.class);

                String keysFromDatabase = snapshot.getKey();

                keys.add(keysFromDatabase);

               // Log.i("key1", keys.get(1));

                chatMessages.add(messagesFromDatabse);



                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        chatListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                new AlertDialog.Builder(ChatActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete the message ?")
                        .setMessage("Are you sure you want to delete the message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int y) {





                                firebaseDatabase.getReference().child("Chats").child(finalString).child(keys.get(i)).removeValue();

                                keys.remove(i);

                                chatMessages.remove(i);

                                arrayAdapter.notifyDataSetChanged();



                            }
                        }).setNegativeButton("No", null)

                        .show();


                        return true;




            }
        });




    }

}








