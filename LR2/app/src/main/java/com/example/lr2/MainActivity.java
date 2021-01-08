package com.example.lr2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lr2.Adapters.CustomAdapter;
import com.example.lr2.Models.Train;

import java.util.ArrayList;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class MainActivity extends AppCompatActivity
{
    private static final String TABLE_NAME = "trains";
    DatabaseHelper dbHelper;
    CustomAdapter customAdapter;
    ListView lv;
    ArrayList<Train> arrayList;
    Train trainItem;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(getString(R.string.mainActionBar));

        lv = (ListView)findViewById(R.id.trainList);
        dbHelper = new DatabaseHelper(this, "trains", 1);

        ShowRecordsInListView();
        ShowCountRecords();
        registerForContextMenu(lv);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean theme = prefs.getBoolean("theme", false);

        if(theme)
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
    }

    public void ShowCountRecords()
    {
        int result = dbHelper.CountRecords();
        TextView tv = (TextView) findViewById(R.id.countRecords);
        {
            tv.setText(Integer.toString(result));
        }
    }

    public void ShowRecordsInListView()
    {
        arrayList = dbHelper.GetAllRecords();
        customAdapter = new CustomAdapter(this, arrayList);
        lv.setAdapter(customAdapter);
        StartTrainClick();
        customAdapter.notifyDataSetChanged();
    }

    public void StartTrainClick()
    {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                Intent intent = new Intent(MainActivity.this, TrainProcessActivity.class);
                trainItem = (Train) lv.getItemAtPosition(position);
                intent.putExtra("item", (Parcelable) trainItem);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.train_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId())
        {
            case R.id.changeTrain:
            {
                trainItem = (Train) lv.getItemAtPosition(info.position);

                Intent intent = new Intent(this, TrainDetailActivity.class);
                intent.putExtra("method", "change");
                intent.putExtra("item", (Parcelable) trainItem);
                startActivity(intent);
                return super.onContextItemSelected(item);
            }

            case R.id.deleteTrain:
            {
                trainItem = (Train) lv.getItemAtPosition(info.position);
                int id = Integer.parseInt(trainItem.toString());

                DatabaseHelper dbHelper = new DatabaseHelper(this,TABLE_NAME, 1);
                dbHelper.DeleteRecordById(TABLE_NAME, id);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return super.onContextItemSelected(item);
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void CreateNewTrainClick(View view)
    {
        Intent intent = new Intent(this, TrainDetailActivity.class);
        intent.putExtra("method", "add");
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        lv = (ListView)findViewById(R.id.trainList);
        dbHelper = new DatabaseHelper(this, "trains", 1);

        ShowRecordsInListView();
        ShowCountRecords();
        super.onResume();
    }


}