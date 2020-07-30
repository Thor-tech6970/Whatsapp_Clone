package com.example.wtf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class usersListActivity extends AppCompatActivity {

    ListView usersListView;

    ArrayList<String> users ;

    ArrayList<String> keys;

    ArrayAdapter arrayAdapter;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference ,userReference;

    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);


        users= new ArrayList<>();

        keys = new ArrayList<>();

        usersListView = (ListView) findViewById(R.id.usersListView);

        arrayAdapter = new ArrayAdapter(usersListActivity.this , android.R.layout.simple_expandable_list_item_1, users);

        usersListView.setAdapter(arrayAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        String userID = firebaseAuth.getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("Users");

        userReference = databaseReference.child(userID);

        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String userNameFromDatabase = snapshot.child("UserName").getValue(String.class);

                String keysFromDatabase = snapshot.getKey();

                users.add(userNameFromDatabase);

                keys.add(keysFromDatabase);

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

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(usersListActivity.this , ChatActivity.class);

                intent.putExtra("Active user" , users.get(i));

                intent.putExtra("User key" ,  keys.get(i));

                startActivity(intent);


            }
        });


    }
}