package com.example.lr1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void ToDetailClick(View view)
    {
        String category = ((Button)view).getText().toString();
        Intent intent = new Intent();
        intent.putExtra("category", category);
        intent.setAction("com.eugene.SHOW_SECOND_ACTIVITY");
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

}