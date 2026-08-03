package com.example.smartparkingfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity
        extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;

    Button btnLogin;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_admin_login
        );

        // Session

        sessionManager =
                new SessionManager(this);

        // Initialize views

        edtEmail =
                findViewById(R.id.edtEmail);

        edtPassword =
                findViewById(R.id.edtPassword);

        btnLogin =
                findViewById(R.id.btnLogin);

        // Login click

        btnLogin.setOnClickListener(v -> {

            loginAdmin();
        });
    }

    private void loginAdmin() {

        String email =
                edtEmail.getText()
                        .toString()
                        .trim();

        String password =
                edtPassword.getText()
                        .toString()
                        .trim();

        // Credentials

        if(email.equals("admin@gmail.com")
                && password.equals("admin123")) {

            sessionManager.setLogin(true);

            Toast.makeText(
                    this,
                    "Login Successful",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(

                    new Intent(
                            this,
                            AdminDashboardActivity.class
                    )
            );

            finish();
        }
        else {

            Toast.makeText(
                    this,
                    "Invalid Credentials",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}