package com.example.lr3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{

    private EditText emailField, passwordField;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Вход");

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.progressBar1);

    }

    public void loginAccount(View view)
    {

        progressBar.setVisibility(View.VISIBLE);

        String email, password;

        email = emailField.getText().toString();

        password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Заполните все поля!", Toast.LENGTH_LONG)
                    .show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(

                        new OnCompleteListener<AuthResult>()
                        {

                            @Override

                            public void onComplete(

                                    @NonNull Task<AuthResult> task)

                            {

                                if (task.isSuccessful())
                                {

                                    Toast.makeText(getApplicationContext(), "Вход выполнен!",
                                            Toast.LENGTH_LONG).show();

                                    progressBar.setVisibility(View.GONE);

                                    Intent intent = new Intent(LoginActivity.this,
                                            ProfileActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else
                                {

                                    Toast.makeText(getApplicationContext(),
                                            "Неверный логин или пароль!",
                                            Toast.LENGTH_LONG).show();

                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                        });
    }

}
