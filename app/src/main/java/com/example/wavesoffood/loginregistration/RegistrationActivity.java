package com.example.wavesoffood.loginregistration;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wavesoffood.MainActivity;
import com.example.foodorder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, confirmPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView loginNow;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        loginNow = findViewById(R.id.loginNow);

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {
        progressBar.setVisibility(View.VISIBLE);
        String email, password,cpassword;

//      email = editTextEmail.getText().toString();
//      password = editTextPassword.getText().toString();

        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());
        cpassword = String.valueOf(confirmPassword.getText());

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(RegistrationActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(RegistrationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(cpassword))
        {
            Toast.makeText(RegistrationActivity.this, "Enter confirm password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6 || password.length() > 15)
        if(!password.equals(cpassword))
        {
            Toast.makeText(getApplicationContext(),"Password length should be between 6 - 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // create new user or register new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                        {
                            Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            //Log.wtf("Hello", "Successsssss!!! ", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Registration failed! Please try again..",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}