package com.example.safebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText emailidet,passwordet;
    Button sigupbutton,loginbutton;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializefields();

        sigupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid=emailidet.getText().toString();
                String password=passwordet.getText().toString();
                mauth.createUserWithEmailAndPassword(emailid,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid=emailidet.getText().toString();
                String password=passwordet.getText().toString();
                mauth.signInWithEmailAndPassword(emailid,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent gotoHomePage= new Intent(MainActivity.this,HomeActivity.class);
                        gotoHomePage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(gotoHomePage);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initializefields() {
        emailidet=(EditText)findViewById(R.id.mailidet);
        passwordet=(EditText)findViewById(R.id.passwordet);
        sigupbutton=(Button)findViewById(R.id.signupbt);
        loginbutton=(Button)findViewById(R.id.loginbt);
        mauth=FirebaseAuth.getInstance();
    }
}