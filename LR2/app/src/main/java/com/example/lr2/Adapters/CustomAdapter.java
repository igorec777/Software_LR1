package com.example.lr2.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.example.lr2.Models.Train;
import com.example.lr2.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Train> arrayList;

    public CustomAdapter(Context context, ArrayList<Train> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Object getItem(int position)
    {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        int textSize = Integer.parseInt(prefs.getString("textSize", "14"));

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.records_list_view, null);
        TextView item = (TextView)convertView.findViewById(R.id.recordItem);

        Train train = arrayList.get(position);
        item.setText(train.getTitle());
        item.setBackgroundColor(train.getColor());
        item.setTextSize(textSize);
        return convertView;
    }

    @Override
    public int getCount()
    {
        return this.arrayList.size();
    }
}
