package com.preesoft.myownmalt.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.preesoft.myownmalt.R;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView loginText;
    private EditText emailET;
    private Button sendEmailButton;
    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_forgot_password);

        loginText = findViewById(R.id.forgot_login_user);
        emailET = findViewById(R.id.forgot_email);
        sendEmailButton = findViewById(R.id.send_email_button);
        progressBarLayout = findViewById(R.id.progress_bar_layout);

        sendEmailButton.setOnClickListener(this);
        loginText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == sendEmailButton) {
            progressBarLayout.setVisibility(View.VISIBLE);
            String email = emailET.getText().toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            sendUserToLoginActivity();
                        }
                        else {
                            Toast.makeText(ForgotPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBarLayout.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            sendUserToLoginActivity();
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}