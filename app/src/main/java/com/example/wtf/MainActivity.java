package com.example.wtf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumberEditText , verificationEditText , usernameEditText;

    FirebaseAuth firebaseAuth;

    PhoneAuthProvider phoneAuthProvider;

    String codeSent;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference , userReference;

    public void sendCode(View view) {

        sendVerificationCode();

    }

    public void login(View view) {

        verifySignInCode();
    }

    private  void verifySignInCode(){

        String codeEntered = verificationEditText.getText().toString();

        if(codeEntered.isEmpty()){

            Toast.makeText(MainActivity.this , "Please enter the verification code" , Toast.LENGTH_SHORT).show();

            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeEntered);

        signInWithPhoneAuthCredential(credential);
    }

    private  void sendVerificationCode() {

        String phoneNumber = phoneNumberEditText.getText().toString();

        if (phoneNumber.length() < 10) {

            Toast.makeText(MainActivity.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();

            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                MainActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

//                Toast.makeText(MainActivity.this,"Auto Verification",Toast.LENGTH_SHORT).show();
//
//                signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override

        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(MainActivity.this,"Code Sent",Toast.LENGTH_SHORT).show();

            codeSent = s;
        }};

    private void signInWithPhoneAuthCredential (PhoneAuthCredential credential){

        firebaseAuth.signInWithCredential(credential)

                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this , "Verification successful ! " , Toast.LENGTH_SHORT).show();

                            String phoneNumber = phoneNumberEditText.getText().toString();

                            String username = usernameEditText.getText().toString();

                            String userID = firebaseAuth.getCurrentUser().getUid();

                            Map<String, Object> dataToSave = new HashMap<>();

                            dataToSave.put("UserName" , username);

                            dataToSave.put("PhoneNumber" , phoneNumber);

                            userReference = databaseReference.child(userID);

                            userReference.updateChildren(dataToSave);

                            Intent intent = new Intent(MainActivity.this , usersListActivity.class);

                            startActivity(intent);





                        } else {

                            Toast.makeText(MainActivity.this , ""+task.getException().getMessage() , Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        phoneAuthProvider = PhoneAuthProvider.getInstance();

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        verificationEditText = findViewById(R.id.verificationEditText);

        usernameEditText = findViewById(R.id.usernameEditText);

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("Users");

        if(firebaseAuth.getCurrentUser() != null){

            Intent intent = new Intent(MainActivity.this , usersListActivity.class);

            startActivity(intent);

        }



    }
}