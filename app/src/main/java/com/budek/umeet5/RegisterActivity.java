package com.budek.umeet5;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;



public class RegisterActivity extends AppCompatActivity{

    protected EditText userName, userMail, userPassword;
    protected Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeButtons();
        initializeEditText();
    }



    private void initializeButtons(){
        register = (Button)findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(userMail.getText().toString(), userPassword.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName.getText().toString())
                                            .build();


                                    task.getResult().getUser().updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                    }
                                                }
                                            });



                                }
                            }
                        });
            }
        });
    }


    private void initializeEditText(){
        userName = (EditText)findViewById(R.id.username);
        userMail = (EditText)findViewById(R.id.email);
        userPassword = (EditText)findViewById(R.id.password);
    }

}