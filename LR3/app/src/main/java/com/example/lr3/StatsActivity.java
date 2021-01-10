package com.example.lr3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lr3.Adapters.CustomAdapter;
import com.example.lr3.Adapters.StatsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StatsActivity extends AppCompatActivity
{
    ListView lv;

    private StatsAdapter statsAdapter;

    private DatabaseReference dbStatsRef;

    private ArrayList<Boolean> statsList;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        getSupportActionBar().setTitle("Статистика");

        lv = (ListView) findViewById(R.id.statsList);

        user = FirebaseAuth.getInstance().getCurrentUser();

        statsList = new ArrayList<>();

        dbStatsRef = FirebaseDatabase.getInstance().getReference("Stats").child(user.getUid());

        readStats();
    }

    public void readStats()
    {
        ValueEventListener vListener = new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                statsList.clear();

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    statsList.add((boolean)ds.getValue());
                }

                statsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };

        dbStatsRef.addValueEventListener(vListener);

        statsAdapter = new StatsAdapter(this, statsList);
        lv.setAdapter(statsAdapter);
    }
}
