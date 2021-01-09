package com.example.lr3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("BattleShip");

        mAuth = FirebaseAuth.getInstance();

    }


    public void loginClick(View view)
    {
        Intent intent;

        if (mAuth.getCurrentUser() == null)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }

    public void registerClick(View view)
    {
        if (mAuth.getCurrentUser() != null)
            FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
