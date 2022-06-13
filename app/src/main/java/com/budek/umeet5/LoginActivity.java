package com.budek.umeet5;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;



public class LoginActivity extends AppCompatActivity {

    protected EditText userMail, userPassword;
    protected Button login;
    protected TextView createAccount;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeFirebase();
        initializeButtons();
        initializeEditText();
        initializeTextViews();
    }


    private void initializeFirebase(){
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }


    private void initializeButtons(){
        login = (Button)findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(userMail.getText().toString(), userPassword.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            }
                        });
            }
        });
    }


    private void initializeEditText(){
        userMail = (EditText)findViewById(R.id.email);
        userPassword = (EditText)findViewById(R.id.password);
    }


    private void initializeTextViews(){
        createAccount = (TextView)findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


}