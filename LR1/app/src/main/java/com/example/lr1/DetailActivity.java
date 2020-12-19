package com.example.lr1;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;

public class DetailActivity extends MainActivity implements AdapterView.OnItemSelectedListener
{
    ClipData clipData;
    ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Spinner spnFrom, spnTo;
        String category;
        int chosenCategory;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        Bundle arguments = getIntent().getExtras();
        category = arguments.getString("category");
        ((TextView)findViewById(R.id.category)).setText(category);


        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (category.equals("Вес и масса"))
            chosenCategory = R.array.weight;
        else if(category.equals("Время"))
            chosenCategory = R.array.time;
        else
            chosenCategory = R.array.distance;

        spnFrom = (Spinner)findViewById(R.id.convertFrom);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource
               (this, chosenCategory, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFrom.setAdapter(adapter1);
        spnFrom.setOnItemSelectedListener(this);

        spnTo = (Spinner)findViewById(R.id.convertTo);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource
                (this, chosenCategory, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTo.setAdapter(adapter2);
        spnTo.setOnItemSelectedListener(this);

    }

    public BigDecimal Convert(String category, int unitFrom, int unitTo, double value)
    {
        double k;
        if(category.equals("Вес и масса"))
            k = Math.pow(1000, unitFrom - unitTo);
        else if(category.equals("Время"))
            k = Math.pow(60, unitFrom - unitTo);
        else
            k = Math.pow(10, unitFrom - unitTo);

        return new BigDecimal(value*k, MathContext.DECIMAL32).stripTrailingZeros();
    }

    public void BackClick(View view)
    {
        finish();
    }

    public void Num1Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "1");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num2Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "2");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num3Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "3");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num4Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "4");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num5Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "5");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num6Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "6");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num7Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "7");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num8Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "8");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num9Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "9");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void Num0Click(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText(t.getText() + "0");
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void ClearOneClick(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        String str = t.getText().toString();

        if(str.length() == 0)
            return;
        else if(str.length() == 2 && str.charAt(0) == '-')
            t.setText("");
        else
            t.setText(str.substring(0, str.length() - 1));
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void ClearAllClick(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        t.setText("");
        ((TextView)findViewById(R.id.resultNum)).setText("0");
    }
    public void MinusClick(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        String str = t.getText().toString();
        if(str.contains("-"))
            return;
        else
            t.setText("-".concat(str));
        if(t.getText().length() > 1)
            onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }
    public void DotClick(View view)
    {
        TextView t = findViewById(R.id.sourceNum);
        String str = t.getText().toString();
        if(str.contains(".") || str.length() == 0 || (str.length() == 1 && str.charAt(0) == '-'))
            return;
        else
            t.setText(str.concat("."));
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        TextView t = findViewById(R.id.sourceNum);
        int unitFrom = 0, unitTo = 0;
        double value;
        BigDecimal result;
        String category;

        if(t.getText().toString().equals(""))
            return;
        value = Double.parseDouble(t.getText().toString());
        category = ((TextView)findViewById(R.id.category)).getText().toString();
        Spinner sp1 = findViewById(R.id.convertFrom);
        Spinner sp2 = findViewById(R.id.convertTo);

        unitFrom = sp1.getSelectedItemPosition();
        unitTo = sp2.getSelectedItemPosition();
        //String str = (String) parent.getItemAtPosition(position);
        result = Convert(category, unitFrom, unitTo, value);
        ((TextView)findViewById(R.id.resultNum)).setText(result.toPlainString());
    }

    public void SwitchUnitsClick(View view)
    {
        int unitFrom, unitTo, temp;

        unitFrom = ((Spinner)findViewById(R.id.convertFrom)).getSelectedItemPosition();
        unitTo = ((Spinner)findViewById(R.id.convertTo)).getSelectedItemPosition();
        ((Spinner)findViewById(R.id.convertFrom)).setSelection(unitTo);
        ((Spinner)findViewById(R.id.convertTo)).setSelection(unitFrom);
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }

    public void CopySourceClick(View view)
    {
        String copyText = ((TextView)findViewById(R.id.sourceNum)).getText().toString();

        clipData = ClipData.newPlainText("text", copyText);
        clipboardManager.setPrimaryClip(clipData);
    }

    public void PasteSourceClick(View view)
    {
        String pasteText;

        ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
        pasteText = item.getText().toString();
        ((TextView)findViewById(R.id.sourceNum)).setText(pasteText);
        onItemSelected((AdapterView<?>) findViewById(R.id.convertFrom), view, 0, 0);
    }

    public void CopyResultClick(View view)
    {
        String copyText = ((TextView)findViewById(R.id.resultNum)).getText().toString();

        clipData = ClipData.newPlainText("text", copyText);
        clipboardManager.setPrimaryClip(clipData);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

}
