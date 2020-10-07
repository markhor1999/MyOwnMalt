package com.preesoft.myownmalt.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.preesoft.myownmalt.R;
import com.preesoft.myownmalt.activities.DashBoardActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private EditText emailET, passwordET;
    private Button loginButton;
    private TextView mForgotPasswordTV;
    private RelativeLayout mProgressBarLayout;
    private boolean isProgressVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("LOGIN");

        firebaseAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.login_email_id);
        passwordET = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        mProgressBarLayout = findViewById(R.id.progress_bar_layout);
        mForgotPasswordTV = findViewById(R.id.login_forgot_password);


        loginButton.setOnClickListener(this);
        mForgotPasswordTV.setOnClickListener(this);

    }

    @Override
    protected void onStart() {

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            sendUserToDashboardActivity();
        }
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            if(!isProgressVisible) {
                mProgressBarLayout.setVisibility(View.VISIBLE);
                isProgressVisible = true;
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressBarLayout.setVisibility(View.INVISIBLE);
                            isProgressVisible = false;
                            sendUserToDashboardActivity();

                        } else {
                            mProgressBarLayout.setVisibility(View.INVISIBLE);
                            isProgressVisible = false;
                            Toast.makeText(LoginActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else if (v == mForgotPasswordTV) {
            sendUserToForgotPasswordActivity();
        }
    }

    private void sendUserToForgotPasswordActivity() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }


    private void sendUserToDashboardActivity() {
        Intent intent = new Intent(this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }
}