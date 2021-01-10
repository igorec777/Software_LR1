package com.example.lr3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity
{
    private EditText emailField, passwordField;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Регистрация");

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.progressBar1);

    }

    public void createAccount(View view)
    {
        String email, password;

        progressBar.setVisibility(View.VISIBLE);

        email = emailField.getText().toString();
        password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Заполните все поля!",
                    Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
        else if (!isEmailValid(email))
        {
            Toast.makeText(getApplicationContext(), "Укажите верный адрес электронной почты",
                    Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {

                        Toast.makeText(getApplicationContext(), "Регистрация прошла успешно!",
                                Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        finish();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Регистрация не удалась!",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public static boolean isEmailValid(String email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}