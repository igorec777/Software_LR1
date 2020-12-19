package com.example.lr2;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lr2.Models.Train;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

public class TrainDetailActivity extends AppCompatActivity implements ColorPickerDialogListener
{
    final String LOG_TAG = "myLogs";
    int color;
    Train trainItem;
    String method;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traindetail);
        getSupportActionBar().setTitle(getString(R.string.trainDetailActionBar));

        Bundle extras = getIntent().getExtras();
        method = extras.getString("method");
        trainItem = extras.getParcelable("item");

        if (trainItem != null)
        {
            ((EditText)findViewById(R.id.title)).setText(trainItem.getTitle());
            color = trainItem.getColor();
            ((EditText)findViewById(R.id.prepTime)).setText(Integer.toString(trainItem.getPrepTime()));
            ((TextView)findViewById(R.id.workTime)).setText(Integer.toString(trainItem.getWorkTime()));
            ((TextView)findViewById(R.id.freeTime)).setText(Integer.toString(trainItem.getFreeTime()));
            ((TextView)findViewById(R.id.cycleNum)).setText(Integer.toString(trainItem.getCycleNum()));
            ((TextView)findViewById(R.id.setNum)).setText(Integer.toString(trainItem.getSetNum()));
            ((TextView)findViewById(R.id.freeOfSet)).setText(Integer.toString(trainItem.getFreeOfSet()));
        }

    }

    public void SaveChanges(View view)
    {

        DatabaseHelper dbHelper = new DatabaseHelper(this, "trains", 1);

        int id;
        String title = ((EditText)findViewById(R.id.title)).getText().toString();
        int prepTime = Integer.parseInt(((EditText)findViewById(R.id.prepTime)).getText().toString());
        int workTime = Integer.parseInt(((EditText)findViewById(R.id.workTime)).getText().toString());
        int freeTime = Integer.parseInt(((EditText)findViewById(R.id.freeTime)).getText().toString());
        int cycleNum = Integer.parseInt(((EditText)findViewById(R.id.cycleNum)).getText().toString());
        int setNum = Integer.parseInt(((EditText)findViewById(R.id.setNum)).getText().toString());
        int freeOfSet = Integer.parseInt(((EditText)findViewById(R.id.freeOfSet)).getText().toString());

        if (method.equals("add"))
            dbHelper.InsertRecord(title, color, prepTime, workTime, freeTime, cycleNum, setNum, freeOfSet);
        else
        {
            id = trainItem.getId();
            dbHelper.ChangeRecord(id, title, color, prepTime, workTime, freeTime, cycleNum, setNum, freeOfSet);
        }

        finish();
    }

    public void ChangeColorClick(View view)
    {
        createColorPickerDialog(1);
    }

    @Override
    public void onColorSelected(int dialogId, int color)
    {
        this.color = color;
    }

    @Override
    public void onDialogDismissed(int dialogId)
    {
        Toast.makeText(this, "Цвет выбран", Toast.LENGTH_SHORT).show();
    }

    private void createColorPickerDialog(int id)
    {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(this);
    }
}
